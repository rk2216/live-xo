import {useState} from "react";
import {Redirect} from "react-router";

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
    return !gameLink ? <ul>
        <li>
            <button
                onClick={(event) => {
                    createRoom(event, "TIC_TAC_TOE");
                }}
            >Tic Tac Toe
            </button>
        </li>
    </ul> : <Redirect
        to={{
            pathname: gameLink,
            state: {userName,
            gameLink}
        }}/>;
};

export default GameList;