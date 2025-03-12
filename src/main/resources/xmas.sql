create database if not EXISTS peluches;
use peluches;

CREATE TABLE IF NOT EXISTS Rol (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(45) NOT NULL
);

-- Crear tabla Usuario
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(45),
    apellido VARCHAR(45),
    direccion VARCHAR(100),
    correo VARCHAR(45) UNIQUE,
    tipo_documento VARCHAR(20),
    numero_documento BIGINT(20),
    password VARCHAR(450)
);

-- Crear tabla TIPO_PAGO
CREATE TABLE IF NOT EXISTS TIPO_PAGO (
    id_tipo_pago INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(45) NOT NULL,
    descripcion VARCHAR(100)
);





CREATE TABLE IF NOT EXISTS usuarios_roles (
    usuario_id BIGINT(20),
    rol_id BIGINT(20),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (rol_id) REFERENCES Rol(id)
);

CREATE TABLE IF NOT EXISTS password_reset_token (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(100) NOT NULL,
    usuario_id BIGINT(20),
    expiry_date TIMESTAMP NOT NULL,              
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS MATERIAL (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(50) NOT NULL,
    Descripcion VARCHAR(100),
    Cantidad INT NOT NULL, 
    Precio DECIMAL(10,2) NOT NULL,
    Categoria VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS product (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    description TEXT NOT NULL,
    stock INT NOT NULL,
    category_id BIGINT NOT NULL,
    imagen LONGBLOB NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE IF NOT EXISTS sale (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    total DOUBLE NOT NULL,
    date DATE NOT NULL,
    usuario_id BIGINT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id VARCHAR(255) NOT NULL,
    sale_id BIGINT NOT NULL,
    amount INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (sale_id) REFERENCES sale(id)
);

CREATE TABLE IF NOT EXISTS shopping_cart (
    id BIGINT PRIMARY KEY,
    product_id VARCHAR(255) NOT NULL,
    usuario_id BIGINT NOT NULL,
    amount INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Crear tabla PEDIDO 
CREATE TABLE IF NOT EXISTS PEDIDO (
   id_pedido BIGINT NOT NULL AUTO_INCREMENT,
   direccion_envio VARCHAR(100),
   valor_total DECIMAL(10,2) NOT NULL,
   id_usuario BIGINT NOT NULL, 
   estado ENUM('PENDIENTE', 'ENTREGADO', 'CANCELADO') NOT NULL,
   PRIMARY KEY (id_pedido),
   FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS DETALLE (
    id_detalle BIGINT NOT NULL AUTO_INCREMENT,
    id_pedido BIGINT NOT NULL,
    id_producto VARCHAR(255) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_detalle),
    FOREIGN KEY (id_pedido) REFERENCES PEDIDO(id_pedido),
    FOREIGN KEY (id_producto) REFERENCES product(id)
);

-- Tabla de Chats: Representa una conversación uno a uno entre dos usuarios.
CREATE TABLE IF NOT EXISTS  chats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    user1_id BIGINT NOT NULL,
    user2_id BIGINT NOT NULL,
    CONSTRAINT fk_user1 FOREIGN KEY (user1_id) REFERENCES usuario(id),
    CONSTRAINT fk_user2 FOREIGN KEY (user2_id) REFERENCES usuario(id)
);

-- Tabla de Messages: Almacena cada mensaje de un chat.
CREATE TABLE IF NOT EXISTS  messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    timestamp DATETIME NOT NULL,
    leido BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_chat FOREIGN KEY (chat_id) REFERENCES chats(id),
    CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES usuario(id)
);


CREATE TABLE IF NOT EXISTS chat_alias (
    id BIGINT NOT NULL AUTO_INCREMENT,
    chat_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    alias VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE KEY unique_chat_alias (chat_id, usuario_id),
    CONSTRAINT fk_chat_alias_chat FOREIGN KEY (chat_id) REFERENCES chats(id),
    CONSTRAINT fk_chat_alias_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);




INSERT IGNORE INTO Rol (id, nombre) VALUES (1,'ROLE_ADMIN');
INSERT IGNORE INTO Rol (id, nombre) VALUES (2,'ROLE_VENDEDOR');
INSERT IGNORE INTO Rol (id, nombre) VALUES (3,'ROLE_CLIENTE');

INSERT IGNORE INTO usuario (id, nombre, apellido, direccion, correo, tipo_documento, numero_documento, password) 
VALUES (1, "andres", "Mora", "cll86a", "moraandres1013@gmail.com", "ti", "12101152", "$2b$12$hhCntuUhyjCQkWNEMF36NeceJ3TRD1bFsIhciOaNPQgeZ7eDly6eC");

INSERT IGNORE INTO usuarios_roles (usuario_id, rol_id) VALUES (1, 1);
