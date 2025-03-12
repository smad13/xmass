document.addEventListener("DOMContentLoaded", function() {
    const abrirCarrito = document.getElementById("abrir-carrito");
    const cerrarCarrito = document.getElementById("cerrar-carrito");
    const carritoSidebar = document.getElementById("carrito-sidebar");
    const carritoContenido = document.getElementById("carrito-contenido");
    const totalPriceElem = document.getElementById("total-price");
    const comprarBtn = document.getElementById("cliente-comprar-btn");
    
    // Si pudieras tener más de un icono, usamos querySelectorAll:
    const iconosCarrito = document.querySelectorAll("#icono-carrito");
    iconosCarrito.forEach(icono => {
        icono.addEventListener("click", function(event) {
            event.stopPropagation();
            mostrarCarrito();
        });
    });

    const cartCount = document.getElementById("cart-count");
    let carrito = [];

    // Función para guardar el carrito en localStorage
    function guardarCarrito() {
        localStorage.setItem("carrito", JSON.stringify(carrito));
    }

    // Función para cargar el carrito desde localStorage
    function cargarCarrito() {
        const carritoGuardado = localStorage.getItem("carrito");
        if (carritoGuardado) {
            carrito = JSON.parse(carritoGuardado);
            actualizarCarrito();
        }
    }

    function mostrarCarrito() {
        carritoSidebar.classList.add("mostrar");
    }

    if (abrirCarrito) {
        abrirCarrito.addEventListener("click", function(event) {
            event.preventDefault();
            event.stopPropagation();
            mostrarCarrito();
        });
    }

    cerrarCarrito.addEventListener("click", function(event) {
        event.stopPropagation();
        carritoSidebar.classList.remove("mostrar");
    });

    document.addEventListener("click", function(event) {
        // Verifica que el click no sea en el sidebar, el enlace o alguno de los iconos
        if (!carritoSidebar.contains(event.target) && 
            !abrirCarrito.contains(event.target) &&
            ![...iconosCarrito].some(icono => icono.contains(event.target))) {
            carritoSidebar.classList.remove("mostrar");
        }
    });

    carritoSidebar.addEventListener("click", function(event) {
        event.stopPropagation();
    });

    // Evento para agregar productos al carrito (asegúrate de que tus botones tengan la clase "agregar-carrito")
    document.querySelectorAll(".agregar-carrito").forEach(boton => {
        boton.addEventListener("click", function(event) {
            event.stopPropagation();
            const id = this.getAttribute("data-id");
            const nombre = this.parentElement.querySelector(".card-title")
                ? this.parentElement.querySelector(".card-title").innerText
                : "";
            // Se extrae el precio eliminando caracteres no numéricos
            const precio = parseFloat(
                this.parentElement.querySelector(".text-muted")
                    ? this.parentElement.querySelector(".text-muted").innerText.replace(/[^\d.]/g, "")
                    : "0"
            );
            const imagen = this.parentElement.parentElement.querySelector("img").src;
            const producto = { id, nombre, precio, imagen, cantidad: 1 };
            const existe = carrito.find(item => item.id === id);

            if (existe) {
                existe.cantidad++;
            } else {
                carrito.push(producto);
            }
            actualizarCarrito();
        });
    });

    function actualizarCarrito() {
        carritoContenido.innerHTML = "";
        let totalCantidad = 0;
        let totalPrecio = 0;
        carrito.forEach(producto => {
            totalCantidad += producto.cantidad;
            totalPrecio += producto.precio * producto.cantidad;
            const item = document.createElement("div");
            item.classList.add("carrito-item");
            item.innerHTML = `
                <img src="${producto.imagen}" alt="${producto.nombre}">
                <div class="carrito-item-info">
                    <p>${producto.nombre}</p>
                    <p>$${producto.precio.toFixed(2)}</p>
                    <div>
                        <button onclick="modificarCantidad('${producto.id}', -1)">-</button>
                        <span>${producto.cantidad}</span>
                        <button onclick="modificarCantidad('${producto.id}', 1)">+</button>
                        <button onclick="eliminarProducto('${producto.id}')">🗑</button>
                    </div>
                </div>
            `;
            carritoContenido.appendChild(item);
        });
        cartCount.innerText = totalCantidad;
        totalPriceElem.innerText = "$" + totalPrecio.toFixed(2);
        comprarBtn.disabled = carrito.length === 0;
        guardarCarrito();
    }

    window.modificarCantidad = function(id, cantidad) {
        const producto = carrito.find(item => item.id === id);
        if (producto) {
            producto.cantidad += cantidad;
            if (producto.cantidad <= 0) {
                carrito = carrito.filter(item => item.id !== id);
            }
            actualizarCarrito();
        }
    }

    window.eliminarProducto = function(id) {
        carrito = carrito.filter(item => item.id !== id);
        actualizarCarrito();
    }
    comprarBtn.addEventListener("click", function() {
        if (carrito.length > 0) {
            // Guardar el carrito en localStorage antes de redirigir
            localStorage.setItem("carrito", JSON.stringify(carrito));
            
            // Redirigir a la página del carrito
            window.location.href = "/cliente/carrito-pedido";
        }
    });

    // Al iniciar la página, se carga el carrito almacenado en localStorage
    cargarCarrito();
});