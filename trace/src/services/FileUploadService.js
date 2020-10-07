import http from "../utils/http-common";

const uploadBranch = (file, onUploadProgress) => {
  let formData = new FormData();

  formData.append("file", file);

  return http.post("/uploadBranchMasterFile", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
    onUploadProgress,
  });
};

const uploadTerminal= (file, onUploadProgress) => {
  let formData = new FormData();

  formData.append("file", file);

  return http.post("/uploadTerminalMasterFile", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
    onUploadProgress,
  });
};
const getFiles = () => {
  return http.get("/files");
};

export default {
  uploadBranch,
  uploadTerminal,
  getFiles,
};
