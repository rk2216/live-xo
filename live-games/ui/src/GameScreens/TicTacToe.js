import React, { useState } from "react";
import SockJsClient from "react-stomp";

const TicTacToe = (props) => {
    const [members, setMembers] = useState([]);
    const [roomHost, setRoomHost] = useState("");
    let clientRef = null;
    const roomId = props.match.params.roomId;
    const userName = props.userName;
    const onConnect = (userName) => {
        clientRef.sendMessage("/app/joingame/" + roomId,
            JSON.stringify({ userName, type: 'JOIN' })
        )
    };

    const onMessageReceived = (payload) => {
        const host = payload.host;
        const user = payload.user;
        const updateMembers = payload.members;

        let messageElement = document.getElementById("messageList");
        let li = document.createElement("li");

        if (user.type === 'JOIN') {
            messageElement.classList.add('event-message');
            li.innerText = user.userName + ' joined!';
        } else if (user.type === 'LEAVE') {
            messageElement.classList.add('event-message');
            li.innerText = user.userName + ' left!';
        }
        setMembers(updateMembers);
        if (roomHost !== host.userName) {
            setRoomHost(host.userName);
        }
        messageElement.appendChild(li);
    }

    return (
        <div id="gameRoot">
            <SockJsClient url='http://localhost:8080/ws' topics={['/topic/public/' + roomId]}
                onMessage={onMessageReceived}
                ref={(client) => { clientRef = client }}
                onConnect={() => onConnect(userName)} />
            <div>
                <h4>User: {userName}</h4>
                <h6>{userName === roomHost ? "Host" : "Joinee"}</h6>
                <hr />
                <button
                    onClick={() => { navigator.clipboard.writeText(window.location.href) }}
                >Copy Link
                </button>
            </div>
            <div className="userGameDisplay">
                <ul id="messageList">
                </ul>
                <div/>
                <ul>
                    {members.map((member, index) => {
                        return <li key={`${member}.${index}`}>
                            {member === roomHost ? <b>{member}</b> : member}
                        </li>
                    })}
                </ul>
            </div>
        </div>
    );
}

export default TicTacToe;