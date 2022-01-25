import React from "react";
import {
    BrowserRouter as Router,
    Switch,
    Route
} from "react-router-dom";
import HomeScreen from "../HomeScreen/HomeScreenContainer";
import GameScreenHome from "../GameScreenHomeContainer";

export default function Routes() {
    return (
        <Router>
            <div>
                <Switch>
                    <Route exact path="/">
                        <HomeScreen />
                    </Route>
                    <Route path="/TIC_TAC_TOE/:roomId"
                           render={(props) => <GameScreenHome {...props} {...{gameName: "TIC_TAC_TOE"}}/>}/>
                </Switch>
            </div>
        </Router>
    );
}


