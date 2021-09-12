import React, { useState } from "react";
import SockJsClient from "react-stomp";

const TicTacToe = (props) => {
    const clientRef = null;
    const onConnect = (roomId, username, isHost) => {
        
    };
    const [enableSockClient, setEnableSockClient] = useState(false);
    // props.location.state === undefined for joiner
    const [userName, setUserName] = useState(props.location.state === undefined ? props.userName : props.location.state.userName);
    const [isHost, setIsHost] = useState(props.location.state !== undefined);
    return (
        <React.Fragment>
            <SockJsClient url='http://localhost:8080/ws' topics={['/topic/game/' + roomId]}
                onMessage={onMessageReceived}
                ref={(client) => { clientRef = client }}
                onConnect={() => onConnect(roomId, username, isHost)} />
            <div>
                <h4>User: {userName}</h4>
                <h6>{isHost === true ? "Host" : "Joinee"}</h6>
                <hr />
                <button
                    onClick={() => { navigator.clipboard.writeText(window.location.href) }}
                >Copy Link
                </button>
                {/*<h5>{window.location.href}</h5>*/}
                {/*<h5>http://localhost:3000{props.location.state.gameLink}</h5>*/}

            </div>
        </React.Fragment>
    );
}

export default TicTacToe;