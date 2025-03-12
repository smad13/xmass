// File: chat.js
  document.addEventListener('DOMContentLoaded', function() {
    // Elementos del DOM
    const chatContainer = document.getElementById('chatContainer');
    const newChatBtn = document.getElementById('newChatBtn');
    const chatModal = document.getElementById('chatModal');
    const closeModal = document.getElementById('closeModal');
    const newChatForm = document.getElementById('newChatForm');
    const chatSlider = document.getElementById('chatSlider');
    const closeSliderBtn = document.getElementById('closeSliderBtn');
    const chatTitle = document.getElementById('chatTitle');
    const chatMessages = document.getElementById('chatMessages');
    const messageInput = document.getElementById('messageInput');
    const sendMessageBtn = document.getElementById('sendMessageBtn');

    // Ejemplo: ID del usuario autenticado (deberías obtenerlo realmente de tu backend)
    let currentUserId = 1;

    let currentChatId = null;
    let stompClient = null;
    let currentSubscription = null;

    // Conectar al WebSocket
    function connectWebSocket() {
      const socket = new SockJS('/ws');
      stompClient = Stomp.over(socket);
      stompClient.connect({}, function(frame) {
        console.log('WebSocket conectado: ' + frame);
        if (currentChatId !== null) {
          subscribeChat(currentChatId);
        }
      });
    }

    // Suscribirse al topic del chat actual
    function subscribeChat(chatId) {
      // Cancelar suscripción anterior para evitar duplicados
      if (currentSubscription) {
        currentSubscription.unsubscribe();
        currentSubscription = null;
      }
      if (stompClient && stompClient.connected) {
        currentSubscription = stompClient.subscribe('/topic/chat.' + chatId, function(messageOutput) {
          const message = JSON.parse(messageOutput.body);
          displayMessage(message);
        });
      }
    }

    // Mostrar un mensaje en el contenedor de mensajes
    function displayMessage(message) {
      const messageElement = document.createElement('div');
      messageElement.classList.add('chat-message');

      // Si el remitente es el usuario actual, alinear a la derecha
      if (message.sender && message.sender.id === currentUserId) {
        messageElement.classList.add('my-message');
      } else {
        messageElement.classList.add('other-message');
      }

      messageElement.textContent = message.content;
      chatMessages.appendChild(messageElement);
    }

    // Enviar mensaje vía WebSocket
    function sendMessage() {
      const msg = messageInput.value;
      if (msg.trim() === '' || currentChatId === null) return;
      const chatMessage = { chatId: currentChatId, content: msg };
      stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
      messageInput.value = '';
    }

    // Actualizar alias vía endpoint PUT
    function updateAlias(chatId, newAlias) {
      return fetch('/api/chats/' + chatId + '/alias?alias=' + encodeURIComponent(newAlias), {
        method: 'PUT'
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Error al actualizar alias');
        }
        return response.json();
      });
    }

    // Cargar la lista de chats desde la base de datos
    function loadChats() {
      fetch('/api/chats')
        .then(response => response.json())
        .then(chats => {
          chatContainer.innerHTML = '';
          chats.forEach(chat => {
            const chatDiv = document.createElement('div');
            chatDiv.classList.add('chat-item');
            chatDiv.dataset.chatId = chat.id;
            const alias = chat.alias ? chat.alias : chat.nombre;

            // Se usan íconos de Font Awesome para editar y eliminar
            chatDiv.innerHTML = `
              <span class="chat-alias">${alias}</span>
              <button class="edit-alias-btn" title="Editar alias">
                <i class="fas fa-pencil-alt"></i>
              </button>
              <button class="delete-chat-btn" title="Eliminar chat">
                <i class="fas fa-trash-alt"></i>
              </button>
            `;

            // Al hacer clic en el elemento (excepto en los botones) se abre el slider
            chatDiv.addEventListener('click', function(e) {
              if (
                e.target.classList.contains('edit-alias-btn') ||
                e.target.classList.contains('delete-chat-btn') ||
                e.target.closest('.edit-alias-btn') ||
                e.target.closest('.delete-chat-btn')
              ) return;
              openChatSlider(chat.id, alias);
            });

            // Evento para editar alias
            chatDiv.querySelector('.edit-alias-btn').addEventListener('click', function(e) {
              e.stopPropagation();
              const newAlias = prompt('Ingresa el nuevo alias para este chat:', alias);
              if (newAlias && newAlias.trim() !== '') {
                updateAlias(chat.id, newAlias)
                  .then(updated => {
                    chatDiv.querySelector('.chat-alias').textContent = updated.alias;
                    if (currentChatId === chat.id) {
                      chatTitle.textContent = updated.alias;
                    }
                  })
                  .catch(err => console.error('Error al actualizar alias:', err));
              }
            });

            // Evento para eliminar chat
            chatDiv.querySelector('.delete-chat-btn').addEventListener('click', function(e) {
              e.stopPropagation();
              if (confirm("¿Estás seguro de eliminar este chat?")) {
                fetch('/api/chats/' + chat.id, {
                  method: 'DELETE'
                })
                .then(response => {
                  if (response.ok) {
                    chatDiv.remove();
                  } else {
                    console.error("Error al eliminar chat");
                  }
                })
                .catch(err => console.error("Error al eliminar chat:", err));
              }
            });

            chatContainer.appendChild(chatDiv);
          });
        })
        .catch(error => console.error("Error al cargar chats:", error));
    }

    // Abrir el slider y cargar historial de mensajes
    function openChatSlider(chatId, chatAlias) {
      // Cancelar suscripción previa
      if (currentSubscription) {
        currentSubscription.unsubscribe();
        currentSubscription = null;
      }
      currentChatId = chatId;
      chatTitle.textContent = chatAlias;
      chatSlider.style.display = 'flex'; // Muestra el slider
      chatMessages.innerHTML = '';
      // Cargar historial de mensajes
      fetch('/api/chats/' + chatId + '/messages')
        .then(response => response.json())
        .then(messages => {
          messages.forEach(msg => displayMessage(msg));
        })
        .catch(error => console.error("Error al cargar historial de mensajes:", error));
      subscribeChat(chatId);
    }

    // Cerrar el slider
    closeSliderBtn.addEventListener('click', function() {
      chatSlider.style.display = 'none';
      if (currentSubscription) {
        currentSubscription.unsubscribe();
        currentSubscription = null;
      }
      currentChatId = null;
    });

    // Mostrar el modal para crear un nuevo chat
    newChatBtn.addEventListener('click', function() {
      chatModal.style.display = 'block';
    });

    // Cerrar el modal de nuevo chat
    closeModal.addEventListener('click', function() {
      chatModal.style.display = 'none';
    });

    // Crear un nuevo chat
    newChatForm.addEventListener('submit', function(e) {
      e.preventDefault();
      const chatName = document.getElementById('chatName').value;
      const user2Correo = document.getElementById('user2Correo').value;
      fetch('/api/chats/crear', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'nombre=' + encodeURIComponent(chatName) + '&user2Correo=' + encodeURIComponent(user2Correo)
      })
      .then(response => response.json())
      .then(newChat => {
        chatModal.style.display = 'none';
        newChatForm.reset();
        loadChats();
      });
    });

    // Enviar mensaje
    sendMessageBtn.addEventListener('click', function() {
      sendMessage();
    });

    // Conectar al WebSocket y cargar la lista de chats al iniciar
    connectWebSocket();
    loadChats();
  });

