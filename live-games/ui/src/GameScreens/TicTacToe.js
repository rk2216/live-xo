import React, { useState } from "react";
import SockJsClient from "react-stomp";

const TicTacToe = (props) => {
    const [members, setMembers] = useState([]);
    const [roomHost, setRoomHost] = useState("");
    let clientRef = null;
    const roomId = props.match.params.roomId;
    const onConnect = (userName) => {
        console.log(clientRef);
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
    const userName = !props.location.state ? props.userName : props.location.state.userName;
    const isHost = !!props.location.state;

    return (
        <React.Fragment>
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
            <div style={{ display: 'flex' }}>
                <ul id="messageList">
                </ul>
                <ul>
                    {members.map((member) => {
                        return <li>
                            {member === roomHost ? <b>{member}</b> : member}
                        </li>
                    })}
                </ul>
            </div>
        </React.Fragment>
    );
}

export default TicTacToe;