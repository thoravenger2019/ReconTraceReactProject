import React, { useState, useEffect } from 'react';
import 'antd/dist/antd.css';
import axios from '../utils/axios';
import MenuSideBar from './menuSideBar';
import {
  Form,
  Button,
  Select,
  Card,
  Row,
  Col,
  Layout,
  Avatar,
  Input,
  Alert,
} from 'antd';
import Title from 'antd/lib/typography/Title';
const { Header, Content } = Layout;
const { Option } = Select;

const ImportFile = props => {
  console.log(props)

  const [data, setData] = useState([])
  const [loader, setLoader] = useState(true)
  const [clientid, setClientID] = useState([])
  const [filetype, setFileType] = useState([])
  const [clientData, setClientData] = useState([])
  const [filestatus,setStatus]=useState([])
  const [selectedFileData, setStateFile] = useState(undefined)
  const [msgFlag,setMsgFlag]=useState(false)
  console.log(filestatus)
  useEffect(() => {
    //onDisplayImplortFile();
    onDisplayClientNameList();
  }, [])

  function onChangeClientName(value) {
    console.log(`selected ${value}`);
    setClientID(value);
    onDisplayImplortFile(value);
  }


  function onChangeFileType(value) {
    console.log(`selected ${value}`);
    setFileType(value);
  }

  const onDisplayClientNameList = async () => {
    try {
      const clientNameResponse = await axios.get(`clientName`);
      console.log(clientNameResponse.data)
      setLoader(false);

      const clientNameN = clientNameResponse.data;
      console.log(clientNameN);

      //const role = JSON.parse(roleN.roleNames);
      //console.log(role);
      //  const firstClient=clientNameN[0];
      const clientNameList = clientNameN.map((item, index) =>
        <Option value={item.id} key={index}>{item.clientNameList}
        </Option>
      )
      setClientData(clientNameList);

    } catch (e) {
      console.log(e)
    }
  };

  const onDisplayImplortFile = async (value) => {
    try {

      // const importFileResponse = await axios.get(`getUploadFiletype`);@GetMapping("getFileType/{clientid}")
      const importFileResponse = await axios.get(`getFileType/${value}`);
      console.log(importFileResponse.data)
      setLoader(false);

      const fileN = importFileResponse.data;
      console.log(fileN);

      const listFile = fileN.map((item, index) => <Option value={item.filename} key={index}>{item.filename}</Option>)
      setData(listFile);

      //console.log(dataAll);

    } catch (e) {
      console.log(e)
    }
  };

  const menuData = props.location.state;
  console.log(menuData);


  const [form] = Form.useForm()


  const onFileUpload = async () => {
    try {
      //alert(filetype);
      //ATM NPCI Filess
      if (filetype == 'ATM_ALL_NPCI' || filetype == 'ATM_ACQUIRER_NPCI' || filetype == 'ATM_ISSUER_NPCI' || filetype == 'ATM_ONUS_NPCI') {
        //  alert("check ")
        let currentFile = selectedFileData;
        const formData = new FormData();
        //formData.append('npci', currentFile);
        for (let i = 0; i < currentFile.length; i++) {
          formData.append(`npci`, currentFile[i])
          console.log(currentFile[i])
        }

        console.log(currentFile);
        console.log(formData);
        const response = await axios.post(`importFileNpciATMFiles/${clientid}/${filetype}`, formData);
        //const response = await axios.post(`importFileNpciATMFiles`,formData);
        // const response = await axios.post(`importPosSettlementSummaryReportFiles/${clientid}`,formData);
        console.log(response.data);
        //console.log(response.data);
        const fileUploadStatus=response.data;
        
        if (filetype == 'ATM_ALL_NPCI' || filetype == 'ATM_ACQUIRER_NPCI')
        {
          const statuss = fileUploadStatus[0];
          //const result
          console.log(statuss);
          setStatus(statuss);
          setMsgFlag(true);

          //alert(statuss);
                   //window.location.reload(false);

        } 
        if(filetype == 'ATM_ALL_NPCI' || filetype == 'ATM_ISSUER_NPCI'){
          const statuss = fileUploadStatus[0];
          //const result
          console.log(statuss);
          setStatus(statuss);
          setMsgFlag(true);
 //window.location.reload(false);
        }
      }

      if (filetype == 'CBS_ATM_ISSUER' || filetype=='CBS_ATM_ALL') {

       //alert("INSEIDE CBS ");

        // let currentFile = selectedFileData[0];
        // const formData = new FormData();
        // formData.append('glCbs', currentFile);


        let currentFile = selectedFileData;
        const formData = new FormData();
        //formData.append('npci', currentFile);
        for (let i = 0; i < currentFile.length; i++) {
          formData.append(`glCbs`, currentFile[i]);
          console.log(currentFile[i]);
        }
        console.log(currentFile);
        console.log(formData);
        const response = await axios.post(`importGlcbsFileData/${clientid}/${filetype}`, formData);
        //const response = await axios.post(`importFileNpciATMFiles`,formData);@PostMapping("importGlcbsFileData/{clientid}/{fileTypeName}")
        // const response = await axios.post(`importPosSettlementSummaryReportFiles/${clientid}`,formData);
        const fileUploadStatus=response.data;
        console.log(fileUploadStatus);
          // const statuss = fileUploadStatus.map((item)=>item.GLCBSSTATUS)
          //             alert(statuss);
           const statuss = fileUploadStatus[0];
          //const result
          console.log(statuss);
          setStatus(statuss);
          setMsgFlag(true);

                   // window.location.reload(false);
      }

      if(filetype=='SWITCH_ATM_ALL' ){

        //alert("INSEIDE SWITCH ");
        // let currentFile = selectedFileData[0];
        // const formData = new FormData();
        // formData.append('sw', currentFile);
        let currentFile = selectedFileData;
        const formData = new FormData();
        //formData.append('npci', currentFile);
        for (let i = 0; i < currentFile.length; i++) {
          formData.append(`sw`, currentFile[i])
          console.log(currentFile[i])
        }

        console.log(currentFile);
        console.log(formData);
        const response = await axios.post(`importSwitchFile/${clientid}/${filetype}`, formData);
        //const response = await axios.post(`importFileNpciATMFiles`,formData);@PostMapping("importGlcbsFileData/{clientid}/{fileTypeName}")
        // const response = await axios.post(`importPosSettlementSummaryReportFiles/${clientid}`,formData);
        console.log(response.data);
        const fileUploadStatus=response.data;
          // const statuss = fileUploadStatus.map((item)=>item.SWITCHSTATUS)
          //          alert(statuss);
          //          // window.location.reload(false);
                   const statuss = fileUploadStatus[0];
                   //const result
                   console.log(statuss);
                   setStatus(statuss);
                   setMsgFlag(true);
         
      }
      if(filetype=='EJ_ATM_ALL'){

        //alert("INSEIDE EJ ");
        let currentFile = selectedFileData[0];
        const formData = new FormData();
        formData.append('ej', currentFile);
        console.log(currentFile);
        console.log(formData);
        const response = await axios.post(`importEJFiledata/${clientid}/${filetype}`, formData);
        //const response = await axios.post(`importFileNpciATMFiles`,formData);@PostMapping("importGlcbsFileData/{clientid}/{fileTypeName}")
        // const response = await axios.post(`importPosSettlementSummaryReportFiles/${clientid}`,formData);
        console.log(response.data);
        const fileUploadStatus=response.data;
          // const statuss = fileUploadStatus.map((item)=>item.EJFILESTATUS)
          //             alert(statuss);
          //             //window.location.reload(false);
                    const statuss = fileUploadStatus[0];
                    //const result
                    console.log(statuss);
                    setStatus(statuss);
                    setMsgFlag(true);
  
                  }
    } catch (e) {
      console.  log(e)
    }
  }
  const onChangeHandler = event => {
    setStateFile(event.target.files)
  }
  //const [componentSize, setComponentSize] = useState('small');

  return (

    <Layout>
      <Header style={{ padding: "20px" }}>
        <Avatar shape="square" style={{ float: "right" }} size="default" src="./max.png" />
        <Title
          level={3} style={{ color: "white" }}>Trace</Title>
      </Header>
      <Layout>
        <MenuSideBar menuData={menuData} />
        <Layout style={{ height: "100vh", backgroundColor: "white" }}>
          <Content style={{ alignmentBaseline: 'center' }}>
            <Card title="Import File" bordered={false} style={{ width: 1600}}  >

              <Form initialValues={{ remember: true }} layout={"vertical"} form={form} size={"large"} >
                <Row gutter={8} >
                  <Col span={12}><b>
                    <Form.Item label="Bank Name" name="clientid" >
                      <Select defaultValue="--select--" style={{ width: 500 }} onChange={onChangeClientName}>
                        {clientData}
                      </Select>
                    </Form.Item>
                  </b></Col>
                </Row>
                <Row gutter={8}>
                  <Col span={12}><b>
                    <Form.Item
                      label="File Type"
                      name="FileType"
                      rules={[{ required: true, message: 'Please input your Role Type!' }]}>
                      <Select defaultValue="--select--" style={{ width: 500 }} onChange={onChangeFileType} >
                        {data}
                      </Select>
                    </Form.Item>
                  </b></Col>
                </Row>
                <Row gutter={8}>
                  <Col span={12}><b>
                    <Form.Item
                      label="File Upload"
                      name="file"
                      rules={[{ required: true, message: 'Please input your File..!' }]}>

                      {/* <Input type={'file'} name="file[]" size={"large"}  onChange={onChangeHandler}  multiple/> */}
                      <Input type={'file'} name="file" size={"large"} style={{ width: 500 }} onChange={onChangeHandler} multiple/>
                      {/* <Input type="file" webkitdirectory="" directory="" /> */}
                    </Form.Item>
                  </b></Col>
                  </Row>
                  <Form.Item >
                    <Button onClick={onFileUpload} size={"large"} type={"primary"} >Upload</Button>
                    <Button style={{ margin: '0 10px' }} >Back</Button>
                  </Form.Item>  
              
                {msgFlag?(
                <Form.Item>
                  <Alert message={"NUMBER OF FAILD ROW : "+filestatus["NUMBER OF FAILD ROWS"]} type="error" style={{ width: 500 }}/>
                  <Alert message={"NUMBER OF INTERRUPTED FILES : "+filestatus["NUMBER OF INTERRUPTED FILES"]} type="error" style={{ width: 500 }} />
                  <Alert message={"NUMBER OF UPLOADED FILES : "+filestatus["NUMBER OF UPLOADED FILES"]} type="info" style={{ width: 500 }}/>   
                  <Alert message={"NUMBER OF UPLOADED ROWS : "+filestatus["NUMBER OF UPLOADED ROWS"]} type="info" style={{ width: 500 }}/>
                  </Form.Item> 
                ):("")} 
              </Form>
            </Card>
          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
};
export default ImportFile;