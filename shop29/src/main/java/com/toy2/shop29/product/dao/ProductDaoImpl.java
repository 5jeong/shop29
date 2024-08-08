package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.ProductDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDaoImpl implements ProductDao {

    @Autowired
    SqlSession session;

    String namespace = "com.toy2.shop29.dao.ProductMapper.";

    //product 1개만 read
    @Override
    public ProductDto select(int product_id){
        return session.selectOne(namespace + "select",product_id); //?가 있다 싶으면 뒤에 Object parameter 사용해야됨
    }

    //전체 product를 read
    @Override
    public List<ProductDto> selectAll() {
        return session.selectList(namespace+"selectAll");
    }

    //write
    @Override
    public int insert(ProductDto product){
        return session.insert(namespace + "insert",product); //map 사용하던가 아니면 product 사용하던가
    }

    //수정
    @Override
    public int update(ProductDto product){
        return session.update(namespace + "update",product);
    }

    //삭제
    @Override
    public int delete(int product_id){
        return session.delete(namespace + "delete",product_id);
    }

    @Override
    public int deleteByBrand(String brand_id) {
        return session.delete(namespace + "deleteByBrand",brand_id);
    }

    //전체 삭제
    @Override
    public int deleteAll() {
        return session.delete(namespace+"deleteAll");
    }

    // 총 product 갯수
    @Override
    public int count() {
        return session.selectOne(namespace+"count");
    }

    // 같은 brand_id를 가진 상품의 수
    @Override
    public int brandCount(String brand_id){
        return session.selectOne(namespace+"brandCount",brand_id);
    }


}





//@Repository
//public class ProductDao {
//
//    @Autowired
//    DataSource ds;
//
//
//    //product_id에 해당하는 제품 하나를 삭제시키는 메서드
//    @Override
//    public int deleteProduct(int product_id) {
//        int rowCnt = 0; //반환결과 담을 변수
//        String sql = "delete from product where product_id= ? ";
//
//        //try-with-resources 사용
//        try (
//                Connection conn = ds.getConnection();
//                PreparedStatement ps = conn.prepareStatement(sql);
//        ) {
//            ps.setInt(1, product_id);
//
//            rowCnt = ps.executeUpdate(); //insert,delete,update에서는 executeUpdate 사용
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return 0; //예외발생하면 0 출력
//        }
//        return rowCnt;
//    }
//
//
//    // product 정보를 product에 저장하는 메서드 (write)
//    @Override
//    public int insertProduct(ProductDto productDto) {
//        int rowCnt = 0;
//
//        String sql = "insert into product values (?,?,?,?,?,?,?,now(),?,now(),?) ";
////        String sql = "insert into product values (?,?,?,?,?,?,?,now(),?) ";
//
//        //try-with-resources 사용
//        try(
//                Connection conn = ds.getConnection();
//                PreparedStatement ps = conn.prepareStatement(sql);
//                ){
//
//
//            ps.setInt(1, productDto.getProduct_id());
//            ps.setString(2, productDto.getProduct_name());
//            ps.setInt(3, productDto.getSmall_category_id());
//            ps.setString(4, productDto.getBrand_id());
//            ps.setInt(5, productDto.getProduct_price());
//            ps.setInt(6, productDto.getSale_ratio());
//            ps.setInt(7, productDto.getIs_exclusive());
////            ps.setDate(8, new java.sql.Date(product.getCreated_date().getTime()));
//            ps.setString(8, productDto.getCreated_id());
////            ps.setDate(10, new java.sql.Date(product.getUpdated_date().getTime()));
//            ps.setString(9, productDto.getUpdated_id());
//
//            rowCnt = ps.executeUpdate();
//        }catch (SQLException e) {
//            e.printStackTrace();
//            return 0;
//        }
//            return rowCnt;
//    }
//
//
//
//    // 하나의 product 정보를 불러오는 메서드 (read)
//    @Override
//    public ProductDto selectProduct(int product_id) {
//        ProductDto productDto = null;
//        String sql = "select * from product where id= ? ";
//
//        //try-with-resources 사용
//        try(
//                Connection conn = ds.getConnection();
//                PreparedStatement ps = conn.prepareStatement(sql);
//
//        ){
//            ps.setInt(1, product_id);
//            ResultSet rs = ps.executeQuery(); //select는 executeQuery
//
//            if (rs.next()) {
//
//                productDto = new ProductDto();
//
//                productDto.setProduct_id(rs.getInt(1));
//                productDto.setSmall_category_id(rs.getInt(2));
//                productDto.setBrand_id(rs.getString(3));
//                productDto.setProduct_name(rs.getString(4));
//                productDto.setProduct_price(rs.getInt(5));
//                productDto.setSale_ratio(rs.getInt(6));
//                productDto.setIs_exclusive(rs.getInt(7));
//                productDto.setCreated_date(new Date(rs.getDate(8).getTime()));
//                productDto.setCreated_id(rs.getString(9));
//                productDto.setUpdated_date(new Date(rs.getDate(10).getTime()));
//                productDto.setUpdated_id(rs.getString(11));
//
//            }
//
//        }catch (SQLException e) {
//            e.printStackTrace();
//            return null; //예외 생기면 null 반환
//        }
//        return productDto;
//    }
//
//
//
//    // 상품정보를 수정할때 사용하는 메서드
//    @Override
//    public int updateProduct(ProductDto productDto) {
//        int rowCnt = 0;
//
//        String sql = "update product " +
//                "set small_category_id = ?, brand_id=?, product_name=?, product_price =?, sale_ratio=?, is_exclusive=?, created_date=?, created_id=?, updated_date=?, updated_id=? " +
//                "where product_id = ? ";
//
//        try (
//                Connection conn = ds.getConnection();
//                PreparedStatement ps = conn.prepareStatement(sql); // SQL Injection공격, 성능향상
//        ){
//            ps.setInt(1, productDto.getSmall_category_id());
//            ps.setString(2, productDto.getBrand_id());
//            ps.setString(3, productDto.getProduct_name());
//            ps.setInt(4, productDto.getProduct_price());
//            ps.setInt(5, productDto.getSale_ratio());
//            ps.setInt(6, productDto.getIs_exclusive());
//            ps.setDate(7, new java.sql.Date(productDto.getCreated_date().getTime()));
//            ps.setString(8, productDto.getCreated_id());
//            ps.setDate(9, new java.sql.Date(productDto.getUpdated_date().getTime()));
//            ps.setString(10, productDto.getUpdated_id());
//            ps.setInt(11, productDto.getProduct_id());
//
//            rowCnt = ps.executeUpdate();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return 0;
//        }
//        return rowCnt;
//    }
//
//    //test할때 적재된 데이터들 모두 지울때 필요
//    public void deleteAll() throws Exception {
//        Connection conn = ds.getConnection();
//
//        String sql = "delete from product";
//
//        PreparedStatement ps = conn.prepareStatement(sql); // SQL Injection공격, 성능향상
//        ps.executeUpdate(); //  insert, delete, update
//    }







