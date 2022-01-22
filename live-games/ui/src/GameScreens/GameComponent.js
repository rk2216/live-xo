import React, { useState } from "react";
import SockJsClient from "react-stomp";
import Box from '@mui/material/Box';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import { FixedSizeList } from 'react-window';
import TicTacToe from "./TicTacToeContainer";
import { updateTicTacToe } from "./TicTacToe";

const renderRow = (members) => (props) => {
    const { index, style } = props;

    return (
        <ListItem style={style} key={index} component="div" disablePadding>
            <ListItemButton>
                <ListItemText primary={members[index]} />
            </ListItemButton>
        </ListItem>
    );
}

const renderChatRow = (chatList) => (props) => {
    const { index, style } = props;
    const { userName, action } = chatList[index];
    const message = `${userName} ${action}`;

    return (
        <ListItem style={style} key={index} component="div" disablePadding>
            <ListItemButton>
                <ListItemText primary={message} />
            </ListItemButton>
        </ListItem>
    );
}

const gameMap = {
    TIC_TAC_TOE: TicTacToe
};

const gameUpdateActionMap = {
    TIC_TAC_TOE: updateTicTacToe
};

const GameComponent = (props) => {
    const [members, setMembers] = useState([]);
    const [roomHost, setRoomHost] = useState("");
    const [chatConsole, setChatConsole] = useState([]);
    const [clientRef, setClientRef] = useState(null);
    const {
        match: {
            params: {
                roomId
            }
        },
        userName,
        gameName,
        actions: {
            updateGame
        }
    } = props;
    const onConnect = (userName) => {
        clientRef.sendMessage("/app/joingame/" + roomId,
            JSON.stringify({ userName, type: 'JOIN' })
        )
        clientRef.sendMessage("/app/ticTacToe/create/" + roomId,
            JSON.stringify({ userName, type: 'JOIN' })
        )
    };
    
    const Game = gameMap[gameName] || null;

    const userChatUpdate = (payload) => {
        const host = payload.host;
        const user = payload.user;
        const updateMembers = payload.members;
        const updatedChatConsole = [...chatConsole];

        if (user.type === 'JOIN') {
            updatedChatConsole.push({
                userName: user.userName,
                action: 'joined'
            });
        } else if (user.type === 'LEAVE') {
            updatedChatConsole.push({
                userName: user.userName,
                action: 'left'
            });
        }
        setMembers(updateMembers);
        setChatConsole(updatedChatConsole);
        if (roomHost !== host.userName) {
            setRoomHost(host.userName);
        }
    }
    const onMessageReceived = (payload) => {
        if(payload.responseType === 'ROOM') {
            userChatUpdate(payload);
        } else {
            gameUpdateActionMap[gameName](payload, updateGame);
        }
    }

    return (
        <div id="gameRoot">
            <SockJsClient url='http://localhost:8080/ws' topics={['/topic/public/' + roomId]}
                onMessage={onMessageReceived}
                ref={(client) => setClientRef(client)}
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
                <Box
                    sx={{ width: '100%', height: 400, maxWidth: 360, bgcolor: 'background.paper' }}
                >
                    <FixedSizeList
                        height={400}
                        width={360}
                        itemSize={46}
                        itemCount={members.length}
                        overscanCount={5}
                    >
                        {renderRow(members, roomHost)}
                    </FixedSizeList>
                </Box>
                <Game {...{clientRef, roomId, roomHost, userName}}/>
                <Box
                    sx={{ width: '100%', height: 400, maxWidth: 360, bgcolor: 'background.paper' }}
                >
                    <FixedSizeList
                        height={400}
                        width={360}
                        itemSize={46}
                        itemCount={chatConsole.length}
                        overscanCount={5}
                    >
                        {renderChatRow(chatConsole)}
                    </FixedSizeList>
                </Box>
            </div>
        </div>
    );
}

export default GameComponent;