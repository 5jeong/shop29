from sqlalchemy import text
from jinja2 import Template
from database import get_db
from urllib.parse import unquote

def get_product_search():
    # HTML template
    html_template = """
    <div class="message user">
    <div class="search-product-container">
    <input class="search-product-input" type="text" onkeypress="requestProductInput(this, event)" required>
    <label class="search-product-label">상품명</label>
    <span></span>
</div>
</div>
    """

    # Using Jinja2 template to render HTML with the order_ids
    template = Template(html_template)
    rendered_html = template.render()
    
    return rendered_html

def search_product(productName):
    productName = unquote(productName)
    try:
        with next(get_db()) as db:
            query = text("SELECT product_id, product_name FROM product WHERE product_name LIKE :productName")
            products = db.execute(query, {"productName": f"%{productName}%"}).fetchall()
        if not products:
            html_template = """<div><strong>해당 상품이 없습니다.</strong></div>"""
            
            template = Template(html_template)
            rendered_html = template.render()
            
            return rendered_html
        # HTML template
        html_template = """
        <div class="select-container">
        <strong>
            상품 검색결과입니다. 클릭 시 해당 상품 페이지로 이동됩니다.
            </strong>
            <div class="btn-container">
                {% for product in products %}
                <button onclick="goToProductPage({{product[0]}})" class="select-btn">{{ product[1] }}</button>
                {% endfor %}
            </div>
        </div>
        """

        # Using Jinja2 template to render HTML with the order_ids
        template = Template(html_template)
        rendered_html = template.render(products=products)
        print(rendered_html)
        
        return rendered_html
    except Exception as e:
        print(e)
    