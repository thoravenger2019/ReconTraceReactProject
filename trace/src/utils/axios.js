import axios from 'axios';

// if (__DEV__) {
//   const AxiosLogger = require('axios-logger');
//   axios.interceptors.request.use(
//     AxiosLogger.requestLogger,
//     AxiosLogger.errorLogger,
//   );
//   axios.interceptors.response.use(
//     AxiosLogger.responseLogger,
//     AxiosLogger.errorLogger,
//   );
// }

const instance = axios.create({
 baseURL: 'http://192.168.1.130:8080/Admin/api/',
 // baseURL: 'http://192.168.1.31:8080/Admin/api/',
 //baseURL: 'http://localhost:8080/Admin/api/',
});

export function axiosGet(url, authToken, ) {
  return instance.get(url);
}


export function getFileToDownload (baseURL) {
  return axios.get(baseURL, {
    responseType: 'arraybuffer',
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// will need to add Post with auth header
export function axiosPost(url, body) {
  return instance.post(url, body);
}

export const setGlobalToken = (token) => {
  instance.defaults.headers.common['Authorization'] = token;
};

export default instance;