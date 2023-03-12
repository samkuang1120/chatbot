$(document).ready(function() {
  var chatHistory = $("#chat-history");
  var inputBox = $("#input-box");
  var chatForm = $("#chat-form");

  // function to add chat message to the history div
  function addChatMessage(message, isUser) {
    if (message === undefined || message === null || message === "") {
        return;
    }

    var messageClass = isUser ? "user-message" : "bot-message";
    var chatMessage = $("<div>").addClass("chat-message " + messageClass).text(message);
    chatHistory.append(chatMessage);
    chatHistory.scrollTop(chatHistory.prop("scrollHeight"));
  }

  // submit form on enter keypress
  inputBox.keypress(function(event) {
    if (event.which === 13) {
      event.preventDefault();
      chatForm.submit();
    }
  });

  // submit form on submit button click
  chatForm.submit(function(event) {
    event.preventDefault();
    var message = inputBox.val().trim();
    if (message !== "") {
      addChatMessage(message, true);
      inputBox.val("");

      // send message to server
      $.ajax({
        type: "POST",
        url: "/api/chat",
        contentType: "application/json",
        data: JSON.stringify({ message: message }),
        success: function(response) {
          addChatMessage(response.message, false);
        },
        error: function(xhr) {
          addChatMessage("Error: " + xhr.responseText, false);
        }
      });
    }
  });

  // get chat history from server
/*  $.ajax({
    type: "GET",
    url: "/api/chatmessages",
    success: function(response) {
      for (var i = 0; i < response.length; i++) {
        var chatMessage = response[i];
        addChatMessage(chatMessage.message, true);
        addChatMessage(chatMessage.response, false);
      }
    },
    error: function(xhr) {
      addChatMessage("Error: " + xhr.responseText, false);
    }
  });*/
});