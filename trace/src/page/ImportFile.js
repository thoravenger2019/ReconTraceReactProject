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
  const [selectedFileData, setStateFile] = useState(undefined)
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

  function onChange(value) {
    console.log(`selected ${value}`);
  }
  function handleChange(value) {
    console.log(`selected ${value}`);
  }

  const [form] = Form.useForm()


  const onFileUpload = async () => {
    try {
      alert(filetype);
      //ATM NPCI Filess
      if (filetype == 'ATM_ALL_NPCI' || filetype == 'ATM_ACQUIRER_NPCI' || filetype == 'ATM_ISSUER_NPCI' || filetype == 'ATM_ONUS_NPCI') {
        //  alert("check ")
        let currentFile = selectedFileData[0];
        const formData = new FormData();
        formData.append('npci', currentFile);
        console.log(currentFile);
        console.log(formData);
        const response = await axios.post(`importFileNpciATMFiles/${clientid}`, formData);
        //const response = await axios.post(`importFileNpciATMFiles`,formData);
        // const response = await axios.post(`importPosSettlementSummaryReportFiles/${clientid}`,formData);
        console.log(response);
      }

      if (filetype == 'CBS_ATM_ISSUER' || filetype=='CBS_ATM_ALL') {

       alert("INSEIDE CBS ");
        let currentFile = selectedFileData[0];
        const formData = new FormData();
        formData.append('glCbs', currentFile);
        console.log(currentFile);
        console.log(formData);
        const response = await axios.post(`importGlcbsFileData/${clientid}/${filetype}`, formData);
        //const response = await axios.post(`importFileNpciATMFiles`,formData);@PostMapping("importGlcbsFileData/{clientid}/{fileTypeName}")
        // const response = await axios.post(`importPosSettlementSummaryReportFiles/${clientid}`,formData);
        console.log(response);
      }

      if(filetype=='SWITCH_ATM_ALL' ){

        alert("INSEIDE SWITCH ");
        let currentFile = selectedFileData[0];
        const formData = new FormData();
        formData.append('sw', currentFile);
        console.log(currentFile);
        console.log(formData);
        const response = await axios.post(`importSwitchFile/${clientid}/${filetype}`, formData);
        //const response = await axios.post(`importFileNpciATMFiles`,formData);@PostMapping("importGlcbsFileData/{clientid}/{fileTypeName}")
        // const response = await axios.post(`importPosSettlementSummaryReportFiles/${clientid}`,formData);
        console.log(response);
      }
      if(filetype=='EJ_ATM_ALL'){

        alert("INSEIDE EJ ");
        let currentFile = selectedFileData[0];
        const formData = new FormData();
        formData.append('ej', currentFile);
        console.log(currentFile);
        console.log(formData);
        const response = await axios.post(`importEJFiledata/${clientid}/${filetype}`, formData);
        //const response = await axios.post(`importFileNpciATMFiles`,formData);@PostMapping("importGlcbsFileData/{clientid}/{fileTypeName}")
        // const response = await axios.post(`importPosSettlementSummaryReportFiles/${clientid}`,formData);
        console.log(response);
      }


    } catch (e) {
      console.log(e)
    }
  }
  const onChangeHandler = event => {
    setStateFile(event.target.files)
  }
  const [componentSize, setComponentSize] = useState('small');

  const onFormLayoutChange = ({ size }) => {
    setComponentSize(size);
  };
  const tailLayout = {
    wrapperCol: { offset: 10 },
  };
  const FormItem = Form.Item;

  function onChange(checkedValues) {
    console.log('checked = ', checkedValues);
  }



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
          <Content>
            <Card title="Import File" bordered={false} style={{ width: 800 }} >

              <Form initialValues={{ remember: true }} layout={"vertical"} form={form} size={"large"} >

                <Row gutter={8}>
                  <Col xs={2} sm={16} md={12} lg={8} xl={10}><b>
                    <Form.Item label="Client Name" name="clientid" >
                      <Select defaultValue="--select--" style={{ width: 300 }} onChange={onChangeClientName}>
                        {clientData}
                      </Select>
                    </Form.Item>
                  </b></Col>
                </Row>
                <Row gutter={8}>
                  <Col xs={2} sm={16} md={12} lg={8} xl={10}><b>
                    <Form.Item
                      label="File Type"
                      name="FileType"
                      rules={[{ required: true, message: 'Please input your Role Type!' }]}>
                      <Select defaultValue="--select--" style={{ width: 300 }} onChange={onChangeFileType} >
                        {data}
                      </Select>
                    </Form.Item>
                  </b></Col>
                </Row>
                <Row gutter={8}>
                  <Col xs={2} sm={16} md={12} lg={8} xl={10}><b>
                    <Form.Item
                      name="file"
                      rules={[{ required: true, message: 'Please input your Branch Name!' }]}>

                      {/* <Input type={'file'} name="file[]" size={"large"}  onChange={onChangeHandler}  multiple/> */}
                      <Input type={'file'} name="file" size={"large"} onChange={onChangeHandler} />
                      {/* <Input type="file" webkitdirectory="" directory="" /> */}

                    </Form.Item>

                  </b></Col>
                  <Form.Item>
                    <Button onClick={onFileUpload} size={"large"} type={"primary"}>Upload</Button>
                    <Button style={{ margin: '0 8px' }} >Back</Button>
                  </Form.Item>
                </Row>
              </Form>
            </Card>
          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
};
export default ImportFile;