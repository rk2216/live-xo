import { hot } from "react-hot-loader/root";
import React from 'react';
import ReactDOM from 'react-dom';
import reportWebVitals from './reportWebVitals';
import Routes from "./routes/Routes";
import { Provider } from "react-redux";
import { store } from "./ReduxStore/CreateStore";
import "./importStyles";

const render = (Component) => ReactDOM.render(
    <Provider store={store}>
        <React.StrictMode>
            <Component />
        </React.StrictMode>
    </Provider>,
    document.getElementById('root')
);

render(hot(Routes));

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
