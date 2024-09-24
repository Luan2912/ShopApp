CREATE DATABASE ShopApp;
USE ShopApp;

--Khách hàng khi muốn mua hàng => Phải đăng ký tài khoản => Bảng users
CREATE TABLE users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(20) NOT NULL,
    address VARCHAR(200) DEFAULT '',
    password VARCHAR(150) NOT NULL DEFAULT '',--Mật khẩu đã mã hóa 
    created_at DATETIME,
    updated_at DATETIME,--Theo dõi users đc chỉnh sửa khi nào.
    is_active TINYINT(1) DEFAULT 1, --Mặc định user đã được kích hoạt. Muốn xóa mềm user có thể để về 0
    -- Kiểu TINYINT chạy từ 0 đén 9
    date_of_birth DATE,
    facebook_account_id INT DEFAULT 0,
    google_account_id INT DEFAULT 0

);

ALTER TABLE users ADD COLUMN role_id INT;

CREATE TABLE roles(
    id INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

ALTER TABLE users ADD FOREIGN KEY(role_id) REFERENCES roles(id);

--Token để truyển vào hệ thống không cần password
CREATE TABLE tokens(
    id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    expiration_date DATETIME,--Thời gian hết hạn của Token
    revoked TINYINT(1) NOT NUll,--Để theo dõi xem token đã bị thu hồi hay chưa.
    expired TINYINT(1) NOT NULL,--Để kiểm tra xem token đã hết hạn hay chưa.
    user_id INT,--Foreign Key(Khóa ngoại)
    FOREIGN KEY(user_id) REFERENCES users(id)

);

--Hỗ trợ đăng nhập từ Facebook và Google
CREATE TABLE social_accounts(
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `provider` VARCHAR(20) NOT NULL COMMENT 'Tên nhà social network',
    `provider_id` VARCHAR(50) NOT NULL,
    `email` VARCHAR(150) NOT NULL COMMENT 'Email tài khoản',
    `name` VARCHAR(150) NOT NULL COMMENT 'Tên người dùng',
    user_id INT,
    FOREIGN KEY(user_id) REFERENCES users(id)

);

-- Bảng danh mục sản phẩm
CREATE TABLE categories(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL DEFAULT '' COMMENT 'Tên danh mục, VD: Đồ điện tử'

);

--Bảng chứa sản phẩm(Product):"laptop lenovo, macbook,..."
CREATE TABLE products(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(350) NOT NULL DEFAULT '' COMMENT 'Tên sản phẩm',
    price FLOAT NOT NULL CHECK(price>=0),
    thumbnail VARCHAR(300) DEFAULT '',--Đường dẫn đến ảnh của sản phẩm
    description LONGTEXT DEFAULT  '',--mô tả sản phẩm
    created_at DATETIME,
    updated_at DATETIME,
    category_id INT,
    FOREIGN KEY(category_id) REFERENCES categories(id)
);

CREATE TABLE product_images(
     id INT PRIMARY KEY AUTO_INCREMENT,
     product_id INT,
     CONSTRAINT fk_product_images_product_id
        FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE

);

--Đặt hàng - orders
CREATE TABLE orders(
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    FOREIGN KEY(user_id) REFERENCES users(id),
    fullname VARCHAR(100) NOT NULL DEFAULT '',
    email VARCHAR(100) NOT NULL DEFAULT '',
    phone_number VARCHAR(20) NOT NULL ,
    --Các trường fullname, email, phone_number trong bảng này có thể không giống với các trường của users
    address VARCHAR(200) NOT NULL,--Địa chỉ nơi gửi
    note VARCHAR(300) DEFAULT '',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20),--Trạng thái đơn hàng
    total_money FLOAT CHECK(total_money>=0)

);

ALTER TABLE orders ADD COLUMN `shipping_method` VARCHAR(100);
ALTER TABLE orders ADD COLUMN `shipping_address` VARCHAR(100);
ALTER TABLE orders ADD COLUMN `shipping_date` DATE;
ALTER TABLE orders ADD COLUMN `tracking_number` VARCHAR(100);--mã vận đơn
ALTER TABLE orders ADD COLUMN `payment_method` VARCHAR(100);

--Xóa 1 đơn hàng => xóa mềm => thêm trường active 
ALTER TABLE orders 
ADD COLUMN active TINYINT(1);

/*Trạng thái đơn hàng chỉ được phép nhận một giá trị cụ thể 
=> Thay đổi kiểu dữ liệu của cột status thành ENUM với các giá trị được phép*/
ALTER TABLE orders 
MODIFY COLUMN status ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled') COMMENT 'Trạng thái đơn hàng';

CREATE TABLE order_details(
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    FOREIGN KEY(order_id) REFERENCES orders(id),
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products(id),
    price FLOAT CHECK(price>=0),
    number_of_products INT CHECK(number_of_products>0),
    total_money FLOAT CHECK(total_money>=0),
    color VARCHAR(20) DEFAULT ''

);
