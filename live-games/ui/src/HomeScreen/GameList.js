import React, {useState} from "react";
import {Redirect} from "react-router";
import {Button} from "@mui/material";

const GameList = ({userName}) => {
    const [gameLink, setGameLink] = useState(null);
    const createRoom = (event, gameType) => {
        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                user: {
                    type: "JOIN",
                    userName
                },
                gameType
            })
        };
        fetch('http://localhost:8080/createRoom', requestOptions)
            .then(response => response.text())
            .then(data => {
                setGameLink(data);
            });
        event.preventDefault();
    };
    return !gameLink ? <div id="gameList">
        <div id="gameListMain">
            <h1>We only have these</h1>
            <ul>
                <li>
                    <Button
                        onClick={(event) => {
                            createRoom(event, "TIC_TAC_TOE");
                        }}
                        variant="contained"
                        color="success"
                    >Tic Tac Toe
                    </Button>
                </li>
                <li>
                    <Button
                        variant="contained"
                        color="success"
                    >The Trivia Show
                    </Button>
                </li>
                <li>
                    <Button
                        variant="contained"
                        color="success"
                    >Pictionary
                    </Button>
                </li>
                <li>
                    <Button
                        variant="contained"
                        color="success"
                    >Coming Soon
                    </Button>
                </li>
            </ul>
        </div>
    </div> : <Redirect
        to={{
            pathname: gameLink
        }}/>;
};

export default GameList;