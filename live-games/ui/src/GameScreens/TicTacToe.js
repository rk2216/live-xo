import classNames from 'classnames';
import React from "react";

const NO_OP = () => {
}
export const updateTicTacToe = (payload, action) => {
    const {
        board,
        players,
        currentTurn,
        winner
    } = payload;
    action({board, players, currentTurn, winner, gameName: 'TIC_TAC_TOE'});
};

const TicTacToe = (props) => {
    const {
        gameState: tiles,
        currentTurn,
        clientRef,
        roomId,
        playerPosition,
        roomHost,
        userName,
        winner
    } = props;
    const isResetButtonHidden = (winner === null || winner === "");
    const onClickHandler = (index) => {
        clientRef.sendMessage("/app/ticTacToe/" + roomId,
            JSON.stringify({index, currentTurn})
        )
    }

    const onResetHandler = () => {
        clientRef.sendMessage("/app/ticTacToe/reset/" + roomId);
    };

    return <main className="background">
        <section className="title">
            <h1>Tic Tac Toe</h1>
        </section>
        <section className="display">
            You are <span className={classNames("display-player", {
            playerX: playerPosition === 'X',
            playerO: playerPosition === 'O'
        })}>{playerPosition}</span>
        </section>
        <section className="display">
            Player <span className={classNames("display-player", {
            playerX: currentTurn === 'X',
            playerO: currentTurn === 'O'
        })}>{currentTurn}</span>'s turn
        </section>
        <section className="container">
            {tiles.map((tile, index) => {
                return <div key={`tile${index}`} className={classNames('tile', {
                    'xTile': tile === 'X',
                    'oTile': tile === 'O',
                    'empty': tile === null
                })} onClick={() => (!tile && playerPosition === currentTurn && !winner) ?
                    onClickHandler(index) : NO_OP()}>{tile}</div>;
            }
            )}
        </section>
        <section
            className="display announcer">{(winner === "T") ? "Draw!" :
            ((winner === "X" || winner === "O") ? ("Winner is " + winner) : "")}</section>
        }
        {userName === roomHost && <section className="controls">
            <button id="reset" onClick={onResetHandler} hidden={isResetButtonHidden}>Reset</button>
        </section>}
    </main>
};

export default TicTacToe;