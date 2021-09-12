import React from "react";
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link
} from "react-router-dom";
import HomeScreen from "../HomeScreen/HomeScreen";
import TicTacToe from "../GameScreens/TicTacToe";


// This site has 3 pages, all of which are rendered
// dynamically in the browser (not server rendered).
//
// Although the page does not ever refresh, notice how
// React Router keeps the URL up to date as you navigate
// through the site. This preserves the browser history,
// making sure things like the back button and bookmarks
// work properly.

export default function Routes() {
    return (
        <Router>
            <div>

                {/*
          A <Switch> looks through all its children <Route>
          elements and renders the first one whose path
          matches the current URL. Use a <Switch> any time
          you have multiple routes, but you want only one
          of them to render at a time
        */}
                <Switch>
                    <Route exact path="/">
                        <HomeScreen />
                    </Route>
                    <Route path="/TIC_TAC_TOE/:roomId"
                           render={(props) => props.location.state===undefined ? <HomeScreen/> : <TicTacToe {...props}/>}/>
                </Switch>
            </div>
        </Router>
    );
}


