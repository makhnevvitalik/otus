import random
from locust import HttpUser, task, between

class ProductUser(HttpUser):
    wait_time = between(5, 15)
    products_id = []

    def on_start(self):
        self.search_products()
        
    @task
    def search_products(self):
        while len(self.products_id) == 0:
            page = random.randint(0, 20000)
            response = self.client.get(f"/v1/products/search?page={page}&size=20", name="/search", timeout=60)
            content = response.json()['content']
            if len(content) == 0:
                continue;
            self.products_id = [product['id'] for product in content]

    @task(10)
    def get_product(self):
        product_id = self.products_id[random.randint(0, len(self.products_id) - 1)]
        self.client.get(f"/v1/products/{product_id}", name="/get", timeout=60)

