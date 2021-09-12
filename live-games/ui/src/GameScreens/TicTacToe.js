import React, {useState} from "react";
import SockJsClient from "react-stomp";

const TicTacToe=(props)=>{
    const [enableSockClient, setEnableSockClient] = useState(false);
    // props.location.state === undefined for joiner
    const [userName, setUserName] = useState(props.location === undefined ? props.userName : props.location.state.userName);
    const [isHost, setIsHost] = useState(props.location !== undefined);
    return (
        <div>
            <h4>User: {userName}</h4>
            <h6>{isHost===true ? "Host" : "Joiner"}</h6>
            <hr/>
            <button
                onClick={() => {navigator.clipboard.writeText(window.location.href)}}
            >Copy Link
            </button>
            {/*<h5>{window.location.href}</h5>*/}
            {/*<h5>http://localhost:3000{props.location.state.gameLink}</h5>*/}

        </div>
    );
}

export default TicTacToe;