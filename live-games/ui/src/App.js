import React from 'react';
import './App.css';
import SockJsClient from 'react-stomp';

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      enableSockClient: false,
      key: null
    }
  }

  clientRef = null;

  onMessageReceived(payload) {
    let message = payload;

    let messageElement = document.createElement('li');
    let username = document.querySelector('#name').value.trim();

    if (message.type === 'JOIN') {
      messageElement.classList.add('event-message');
      message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
      messageElement.classList.add('event-message');
      message.content = message.sender + ' left!';
    } else if (message.type === 'INVALID') {
      if (username === message.sender) {
        messageElement.classList.add('event-message');
        message.content = 'Invalid link';
      }
    } else {
      if (username === message.sender) {
        messageElement.classList.add('event-message');
        message.content = 'Room is full';
      }
    }
    let messageArea = document.querySelector('#messageArea');
    let textElement = document.createElement('p');
    let messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
  }

  connect(event) {
    let username = document.querySelector('#name').value.trim();
    let key = document.querySelector('#link').value.trim();
    let messageElement = document.createElement('li');
    console.log(event);
    if (username && key) {
      const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ sender: username, type: 'JOIN' })
      };
      fetch('http://localhost:8080/validate/' + key, requestOptions)
        .then(response => response.json())
        .then(data => {
          if (!(data === "INVALID" || data === "FULL")) {
            this.setState({
              enableSockClient: true,
              key,
              username
            });
          } else {
            const message = {};
            if (data === "INVALID") {
              messageElement.classList.add('event-message');
              message.content = 'Invalid link';
            } else {
              messageElement.classList.add('event-message');
              message.content = 'Room full';
            }
            let messageArea = document.querySelector('#messageArea');
            let textElement = document.createElement('p');
            let messageText = document.createTextNode(message.content);
            textElement.appendChild(messageText);

            messageElement.appendChild(textElement);

            messageArea.appendChild(messageElement);
            messageArea.scrollTop = messageArea.scrollHeight;
          }

        });
    }
    event.preventDefault();
  }

  onConnect(key, username) {
    if (key) {
      console.log(this.clientRef);
      this.clientRef.sendMessage("/app/joingame/" + key,
        JSON.stringify({ sender: username, type: 'JOIN' })
      )
    } else {
      console.log(this.clientRef);
      this.clientRef.sendMessage("/app/hostgame",
        JSON.stringify({ sender: username, type: 'JOIN' })
      )
    }
  }

  hostGame() {
    let username = document.querySelector('#name').value.trim();
    if (username) {
      this.setState({
        enableSockClient: true,
        username: username
      });
    }
  }

  render() {
    const { enableSockClient, key, username } = this.state;
    return <div className="App">
      {enableSockClient && <SockJsClient url='http://localhost:8080/ws' topics={['/topic/public']}
        onMessage={this.onMessageReceived.bind(this)}
        ref={(client) => { this.clientRef = client }}
        onConnect={() => this.onConnect(key, username)} />}
      <header className="App-header">
        <form id="usernameForm" name="usernameForm">
          <div className="form-group">
            <input type="text" id="name" placeholder="Username" autoComplete="off" className="form-control" />
            <input type="text" id="link" placeholder="Link" autoComplete="off" className="form-control" />
          </div>
          <div className="form-group">
            <button
              type="button"
              className="accent username-submit"
              onClick={this.hostGame.bind(this)}
              name="host"
              value="host">
              Host Game
            </button>
            <button
              type="button"
              className="accent username-submit"
              name="join"
              onClick={this.connect.bind(this)}
              value="join">
              Join Game
            </button>
          </div>
        </form>
        <ul id="messageArea">

        </ul>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>;
  };
}

export default App;
