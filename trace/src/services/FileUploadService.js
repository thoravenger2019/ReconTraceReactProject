import http from "../utils/http-common";

const uploadBranch = (file, clientID) => {
  alert(clientID);
  let formData = new FormData();

  formData.append("file", file);

  return http.post("/uploadBranchMasterFile", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },  
  });
};

const uploadTerminal= (file, clientID) => {
  alert(clientID);
  let formData = new FormData();

  formData.append("file", file);

  return http.post("/uploadTerminalMasterFile", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};
const getFiles = () => {
 // return http.get("/files");
};

export default {
  uploadBranch,
  uploadTerminal,
 // getFiles,
};
