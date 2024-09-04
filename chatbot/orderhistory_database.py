from sqlalchemy import text
from jinja2 import Template
from database import get_db

def get_order(user_id):
    db = next(get_db())
    query = text("SELECT order_id FROM OrderHistory WHERE user_id = :user_id")
    try:
        histories = db.execute(query, {"user_id": user_id}).fetchall()
        order_ids = [history[0] for history in histories]
        # HTML template
        html_template = """
        <div class="select-container">
            {{user_id}}님의 주문내역입니다.
            <div class="btn-container">
                {% for order_id in order_ids %}
                <button onclick="requestUserOrderHistory(this)" class="select-btn order-history" id="{{ order_id }}">{{ order_id }}</button>
                {% endfor %}
            </div>
        </div>
        """

        # Using Jinja2 template to render HTML with the order_ids
        template = Template(html_template)
        rendered_html = template.render(order_ids=order_ids, user_id=user_id)
        
        return rendered_html
    except Exception as e:
        print(e)
        return f"해당 주문번호는 존재하지 않는 주문번호입니다."
    
    
def get_order_history(user_id, order_id):
    db = next(get_db())
    query = text("""
    SELECT DISTINCT
        ohi.order_id,
        o.order_time,
        p.product_name,
        ov.option_value_name,
        ohi.quantity,
        ohi.product_price,
        ohi.product_order_status
    FROM 
        orderhistoryitem ohi
    JOIN 
        product p ON ohi.product_id = p.product_id
    JOIN
        orderhistory o ON ohi.order_id = o.order_id AND ohi.user_id = o.user_id
    JOIN 
        optionvalue ov ON ohi.product_option_id = ov.option_value_id
    WHERE 
        ohi.user_id = :user_id
        AND ohi.order_id = :order_id;
    """)
    try:
        purchases = db.execute(query, {"user_id": user_id, "order_id" : order_id}).fetchall()
        purchases_detail = "\n".join(
        [
            f"해당 주문 내역입니다.\n",
            f"주문 번호 : {purchases[0][0]}",
            f"주문 일자 : {purchases[0][1]}",
            "\n".join([f"\n상품명 : {p[2]}\n옵션 : {p[3]}\n구매 수량 : {p[4]}\n개당 가격 : {p[5]}\n주문 상태 : {p[6] if p[6] else '결제 완료'}" for p in purchases])
        ]
        )
        html_template="""
        <div class="message assistant">
    <strong>Assistant:</strong>
    <p>{{purchases_detail}}</p>
    </div>"""
        template = Template(html_template)
        rendered_html = template.render(purchases_detail=purchases_detail)
        
        return rendered_html
    except Exception as e:
        print(e)
        return f"해당 주문번호는 존재하지 않는 주문번호입니다."