import React, { useState, useEffect } from 'react';
import 'antd/dist/antd.less'
import 'antd/dist/antd.css';
import axios, { axiosGet,getFileToDownload} from '../utils/axios';
import Axios from "axios";
import MenuSideBar from './menuSideBar';
import {  Avatar, Skeleton,Select } from 'antd';
import '../App';
import UploadService from "../services/FileUploadService";

import {
  Form,
  Input,
  Button,
  Card,
  Layout,
  Space,
  Icon,
 Col,
 Row,
} from 'antd';
import Title from 'antd/lib/typography/Title';
import {
  DownloadOutlined,
} from '@ant-design/icons';

const { Option } = Select;
const { Header, Footer, Sider, Content } = Layout;

let TERA;
const TerminalBranchUpload = props => {

  const [form] = Form.useForm()
  //const [prog, setProgress] = useState(true)
  const [clientdata, setClientData] = useState([])
  const [clientId, setClientId] = useState([])
  const [TerminalFile,setTerminalMasterFile]=useState(undefined);
  const [BranchFile,setBranchMasterFile]=useState(undefined);
  const [loader, setLoader] = useState(true)

  console.log(clientId);
  console.log(TerminalFile)
  console.log(BranchFile)
  useEffect(() => {
    onDisplayClientNameList();
  }, [])
//....................................................................
  const [selectedFiles, setSelectedFiles] = useState(undefined);
  const [currentFile, setCurrentFile] = useState(undefined);
  const [progress, setProgress] = useState(0);
  const [message, setMessage] = useState("");

  const [fileInfos, setFileInfos] = useState([]);
  
  const onBranchFile = event => { 
    // Update the state 
    setBranchMasterFile(event.target.files);
  }; 

  const onTerminalFile = event => { 
    // Update the state 
    setTerminalMasterFile(event.target.files);
  };
  const onUploadBranch= async () =>{

    let currentFile = BranchFile[0];

    setProgress(0);
    setCurrentFile(currentFile);

    UploadService.uploadBranch(currentFile, (event) => {
      setProgress(Math.round((100 * event.loaded) / event.total));
    })
      .then((response) => {
        setMessage(response.data.message);
        console.log(response.data)
        const branchIdresponse=response.data;
        if(JSON.stringify(branchIdresponse)==="[ ]"){
          alert("branch imported successfully")
        }
        else{
          alert("already exist"+(JSON.stringify(branchIdresponse)))
        }

        return UploadService.getFiles();
      })
      .then((files) => {
        setFileInfos(files.data);
      })
      .catch(() => {
        setProgress(0);
        setMessage("Could not upload the file!");
        setCurrentFile(undefined);
      });

    setSelectedFiles(undefined);
  };

  const onUploadTerminal= async () =>{

    let currentFile = TerminalFile[0];

    setProgress(0);
    setCurrentFile(currentFile);

    UploadService.uploadTerminal(currentFile, (event) => {
      setProgress(Math.round((100 * event.loaded) / event.total));
    })
      .then((response) => {
        setMessage(response.data.message);
        console.log(response.data)
        const terminalIdresponse=response.data;
        if(JSON.stringify(terminalIdresponse)==="[ ]"){
          alert("terminal imported successfully")
        }
        else{
          alert("already exist"+(JSON.stringify(terminalIdresponse)))
        }
        return UploadService.getFiles();
      })
      .then((files) => {
        setFileInfos(files.data);
      })
      .catch(() => {
        setProgress(0);
        setMessage("Could not upload the file!");
        setCurrentFile(undefined);
      });

    setSelectedFiles(undefined);
  };
  //........................................................................................
  const onDisplayClientNameList = async () => {
    try {
        const clientNameResponse = await axios.get(`clientName`);
        console.log(clientNameResponse.data)
        setLoader(false);
        const clientNameN = clientNameResponse.data;
        console.log(clientNameN);
        //const role = JSON.parse(roleN.roleNames);
        //console.log(role);
        const clientNameList =clientNameN.map((item, index) =>
        <Option value={item.id} key={index}>{item.clientNameList}
        </Option>
        )
        setClientData(clientNameList);
    } catch (e) {
        console.log(e)
    }
};

  const onTerminalBranch = async () => {
    try {

      const validateFields = await form.validateFields()     
      const values = form.getFieldsValue();
      
      onUploadBranch();
      onUploadTerminal();
      

    } catch (e) {
      console.log(e)
    }
  };

  // props.history.push("/AccessRight", menuData)
  function onChange(value) {
    console.log(`selected ${value}`);
    setClientId(value);
  }
  const onBranchMaster = async () => {
    try {
      getFileToDownload(`http://localhost:8080/Admin/api/branchTemplate/${clientId}`)
      .then (response => {
      const type = response.headers['content-type']
      const blob = new Blob([response.data], { type: type, encoding: 'UTF-8' })
      const link = document.createElement('a')
      link.href = window.URL.createObjectURL(blob)
      link.download = 'BranchMaster.xlsx'
      link.click()
    })     
    } catch (e) {
      console.log(e)
    }
  };

  const onTerminalMaster = async () => {
    try {
      getFileToDownload(`http://localhost:8080/Admin/api/terminalTemplate/${clientId}`)
      .then (response => {
      const type = response.headers['content-type']
      const blob = new Blob([response.data], { type: type, encoding: 'UTF-8' })
      const link = document.createElement('a')
      link.href = window.URL.createObjectURL(blob)
      link.download = 'TerminalMaster.xlsx'
      link.click()
    })     
    } catch (e) {
      console.log(e)
    }
  };

  
  const tailLayout = {
    wrapperCol: { offset: 10 },
  };

  const menuData = props.location.state;

  return (
    <Layout>
      <Header style={{ padding: "20px" }}>
        <Avatar shape="square" style={{ float: "right" }} size="default" src="./max.png" />
        <Title level={3} style={{ color: "white" }}>Trace</Title>
      </Header>
      <Layout>
        <MenuSideBar menuData={menuData} />
        <Layout style={{ height: "100vh", backgroundColor: "white" }}>
       
            <Skeleton active={loader} loading={loader}>
            <Card title="Client Resgistraion" bordered={false} style={{ width: 1000 }} >
                                <Form initialValues={{ remember: true }} layout={"vertical"} form={form}>
                    <Form.Item label="Client Name" name="clientName" >
                  
                    <Select defaultValue="select"  style={{ width: 435 }} onChange={onChange}>
                                       <Option value="select">--select--</Option> 
                      {clientdata}
                    </Select>                     
                  </Form.Item>
                 
                  <Row gutter={8} layout="inline">
                                        <Col flex={1}><b>
                                            <Form.Item
                                                label="Branch Master" name="BranchMaster"                                                
                                                rules={[{ required: true, message: 'Please input your Branch Master!' }]}>
                                                <Input size={"large"} type={'file'}  onChange={onBranchFile} />
                                            </Form.Item>
                                        </b></Col>
                                        <Col flex={4}><b>
                                            <Form.Item
                                              label="  "
                                              name=" ">
                                             <Button type="primary" shape="circle" onClick={onBranchMaster} icon={<DownloadOutlined />}   />
                                               
                                            </Form.Item>
                                        </b></Col>
                                    </Row>
                 
                                    <Row gutter={8} layout="inline">
                                        <Col flex={1}><b>
                                            <Form.Item
                                                label="Terminal Master"
                                                name="TerminalMaster"                                                
                                                rules={[{ required: true, message: 'Please input your Client name!' }]}>
                                                <Input size={"large"}  placeholder="Enter Terminal Master" type={'file'}
                                                  onChange={onTerminalFile}                                               
                                                />
                                            </Form.Item>
                                        </b></Col>
                                        <Col flex={4}><b>
                                            <Form.Item
                                              label="  "
                                              name=" ">
                                             <Button type="primary" shape="circle" onClick={onTerminalMaster} icon={<DownloadOutlined />}   />
                                               
                                            </Form.Item>
                                        </b></Col>
                                    </Row>                 
                                    <Row>
                                            <Form.Item>
                                                <Button type="primary" onClick={onTerminalBranch}>Submit</Button>
                                                <Button style={{ margin: '0 8px', color: 'black' }} onClick={props.history.goBack} >Back</Button>
                                            </Form.Item>
                                    </Row>
                              </Form>        
                          </Card>
                    </Skeleton>
        </Layout>
      </Layout>
    </Layout>
  );
};
export default TerminalBranchUpload;