import React, { useState } from "react";
import SockJsClient from "react-stomp";

const TicTacToe = (props) => {
    let clientRef = null;
    const onConnect = ( userName) => {
            console.log(clientRef);
            clientRef.sendMessage("/app/joingame/" + props.match.params.roomId,
                JSON.stringify({ userName, type: 'JOIN' })
            )
    };

    const onMessageReceived= (payload) => {
        let message = payload;

        let messageElement = document.getElementById("messageList");
        let li = document.createElement("li");

        if (message.type === 'JOIN') {
            messageElement.classList.add('event-message');
            li.innerText = message.userName + ' joined!';
        } else if (message.type === 'LEAVE') {
            messageElement.classList.add('event-message');
            li.innerText = message.userName + ' left!';
        }
        messageElement.appendChild(li);
    }
    const userName = !props.location.state ? props.userName : props.location.state.userName;
    const isHost = !!props.location.state;

    return (
        <React.Fragment>
            <SockJsClient url='http://localhost:8080/ws' topics={['/topic/public']}
                onMessage={onMessageReceived.bind(this)}
                ref={(client) => { clientRef = client }}
                onConnect={() => onConnect(userName)} />
            <div>
                <h4>User: {userName}</h4>
                <h6>{isHost ? "Host" : "Joinee"}</h6>
                <hr />
                <button
                    onClick={() => { navigator.clipboard.writeText(window.location.href) }}
                >Copy Link
                </button>
            </div>
            <ul id="messageList">
            </ul>
        </React.Fragment>
    );
}

export default TicTacToe;