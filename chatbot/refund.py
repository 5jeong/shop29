from sqlalchemy import text
from jinja2 import Template
from database import get_db

def get_refundable_orders(user_id):
    db = next(get_db())
    query = text("SELECT order_id FROM OrderHistory WHERE user_id = :user_id AND order_status IS NOT NULL AND order_status != '환불 완료'")
    try:
        purchases = db.execute(query, {"user_id": user_id}).fetchall()
        if not purchases:
            html_template = """<div><strong>환불 가능한 주문내역이 없습니다.</strong></div>"""
            
            template = Template(html_template)
            rendered_html = template.render()
            
            return rendered_html
        order_ids = [purchase[0] for purchase in purchases]
        # HTML template
        html_template = """
        <div class="select-container">
        <strong>
            환불할 주문내역을 선택해주세요.
            </strong>
            <div class="btn-container">
                {% for order_id in order_ids %}
                <button onclick="requestUserRefundableList(this)" class="select-btn order-history" id="{{ order_id }}">{{ order_id }}</button>
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
    
def get_refundable_order_history(user_id, order_id):
    db = next(get_db())
    query = text("""
    SELECT DISTINCT
        ohi.order_id,
        o.order_time,
        p.product_name,
        ov.option_value_name,
        ohi.quantity,
        ohi.product_price,
        ohi.product_order_status,
        o.order_status,
        ohi.product_id,
        ohi.product_option_id
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
        html_template="""
        <div class="message assistant">
    <strong>Assistant:</strong>
    <p>해당 주문 내역입니다.</p>
    <p>주문 번호 : {{ purchases[0][0] }}</p>
    <p>주문 일자 : {{ purchases[0][1] }}</p>
    {% for p in purchases %}
        {% if p[6] == '취소 완료' %}
            <div class="order-product-container">
                <p>상품명 : {{ p[2] }}</p>
                <p>옵션 : {{ p[3] }}</p>
                <p>구매수량 : {{ p[4] }}</p>
                <p>개당 가격 : {{ p[5] }}</p>
                <p>주문 상태 : {{ p[6] if p[6] else '결제 완료' }}</p>
            </div>
        {% else %}
            <button value="{{ p[8] ~ '|' ~ p[9] ~ '|' ~ p[4] }}" onclick="buttonSelected(this)" class="order-product-btn">
                <p>상품명 : {{ p[2] }}</p>
                <p>옵션 : {{ p[3] }}</p>
                <p>구매수량 : {{ p[4] }}</p>
                <p>개당 가격 : {{ p[5] }}</p>
                <p>주문 상태 : {{ p[6] if p[6] else '결제 완료' }}</p>
            </button>
        {% endif %}
    {% endfor %}
    {% if purchases[0][7] != '환불 완료' %}
    <button class="refund-btn" type="button" order-id={{ purchases[0][0] }}
        onclick="refundRequest(this)">환불 신청
    </button>
        {% endif %}
</div>
"""
        template = Template(html_template)
        rendered_html = template.render(purchases=purchases)
        
        return rendered_html
    except Exception as e:
        print(e)
        return f"해당 주문번호는 존재하지 않는 주문번호입니다."