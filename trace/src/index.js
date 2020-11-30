import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import 'antd/dist/antd.css';
import 'antd/dist/antd.js';
import * as serviceWorker from './serviceWorker';
import AppRouter from './utils/router';
import { BrowserRouter } from 'react-router-dom';
ReactDOM.render(
 
  <BrowserRouter>
    <AppRouter />
  
    </BrowserRouter>,

  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
