document.addEventListener('DOMContentLoaded', () => {

  const sideMenu = document.querySelector('aside');
  const menuBtn = document.querySelector('#menu-btn');
  const closeBtn = document.querySelector('#close-btn');
  const accesos = document.querySelector('.accesos');  // Acceso al contenedor .accesos
  const themeToggleBtn = document.querySelector('.theme-toggler');
  
  // Mostrar barra interior
  menuBtn.addEventListener('click', () => {
    sideMenu.style.display = 'block';
  });
  
  // Cierre barra interior
  closeBtn.addEventListener('click', () => {
    sideMenu.style.display = 'none';
  });
  
  // Cambio de tema
  themeToggleBtn.addEventListener('click', () => {
      // Alternar la clase de tema oscuro en el body y en #accesos
      document.body.classList.toggle('dark-theme-variables');
      accesos.classList.toggle('dark-mode');  // Aplicar tema oscuro también en accesos

      // Alternar los íconos de modo claro y oscuro
      themeToggleBtn.querySelector('span:nth-child(1)').classList.toggle('active');
      themeToggleBtn.querySelector('span:nth-child(2)').classList.toggle('active');
      
      // Guardar el tema en localStorage para que se mantenga después de recargar
      if (document.body.classList.contains('dark-theme-variables')) {
          localStorage.setItem('theme', 'dark');
      } else {
          localStorage.setItem('theme', 'light');
      }
  });

  // Recuperar el tema desde localStorage al cargar la página
  if (localStorage.getItem('theme') === 'dark') {
      document.body.classList.add('dark-theme-variables');
      accesos.classList.add('dark-mode');  // Asegura que accesos tenga el tema oscuro al recargar
      themeToggleBtn.querySelector('span:nth-child(1)').classList.remove('active');
      themeToggleBtn.querySelector('span:nth-child(2)').classList.add('active');
  }
});
