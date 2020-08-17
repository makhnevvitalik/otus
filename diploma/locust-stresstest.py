import random
from locust import HttpUser, task, between


class ShopUser(HttpUser):
    wait_time = between(5, 15)
    products_id = []
    email = None

    def on_start(self):
        self.search_products()

    @task(4)
    def search_products(self):
        while len(self.products_id) == 0:
            page = random.randint(0, 7000)
            response = self.client.get(f'/v1/products/search?page={page}&size=10', name='search products', timeout=30)
            #print(response.json())
            content = response.json()['content']
            if len(content) == 0:
                continue
            self.products_id = [product['id'] for product in content]

    @task(10)
    def get_product(self):
        product_id = self.products_id[random.randint(0, len(self.products_id) - 1)]
        self.client.get(f'/v1/products/{product_id}', name='get product', timeout=30)

    @task(2)
    def create_order(self):
        if self.email is None:
            self.email = self.registration()

        ordered_products = self.products_id[0:len(self.products_id) - 1]
        payload = {'products': [{'id': p, 'count': 1} for p in ordered_products], 'address': self.get_address()}
        headers = {'content-type': 'application/json', 'X-Auth-Request-Email': self.email}

        response = self.client.post('/v1/orders', json=payload, headers=headers, name='create order', timeout=30)
        order_id = response.json()['id']
        response = self.client.post(f'/v1/payments/{order_id}/pay?status=OK', name='pay order', timeout=30)
        #print(response.json())

    def registration(self):
        email = f'{random.randint(0, 1000000)}@mail.ru'

        payload = {'name': f'Name {random.randint(0, 1000000)}', 'phone': '1234567890', 'address': self.get_address()}
        headers = {'content-type': 'application/json', 'X-Auth-Request-Email': email}

        response = self.client.put('/v1/customers/current', json=payload, headers=headers, name='create account', timeout=30)
        return email

    def get_address(self):
        if random.randint(0, 10) == 10:
            return 'Москва'
        else:
            return 'Екатеринбург'
