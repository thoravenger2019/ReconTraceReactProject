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
  DatePicker,
  Input,
  spin,
  Spin,

} from 'antd';
import Title from 'antd/lib/typography/Title';
const { Header, Content } = Layout;
const { Option } = Select;

const RunRecon = props => {
  console.log(props)

  const [data, setData] = useState([])
  const [loader, setLoader] = useState(true)
  const [clientid, setClientId]=useState([])
  const [clientData,setClientData]=useState([])
  const [channelData,setChannelData]=useState([])
  const [modeData,setModeData]=useState([])
  const [spindata,setSpinData]=useState(false)
  const [reconmsg,setReconMsg]=useState([])
  const [selectedFileData, setStateFile] = useState(undefined)
  useEffect(() => {
    //onDisplayImplortFile();
    onDisplayClientNameList();
  }, [])

  function onChangeClientName(value) {
    console.log(`selected ${value}`);  
    setClientId(value);
    onDisplayChannel(value); 
  }

  function onChangeMode(value) {
    console.log(`selected ${value}`);
    
    //setClientId(value);
   // onDisplayChannel(value); 
  }
  function onChangeChannel(value) {
    console.log(`selected ${value}`);  
    //setClientId(value);
    ongetModeType(value); 
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
        const clientNameList =clientNameN.map((item, index) =>
        <Option value={item.id} key={index}>{item.clientNameList}
        </Option>
        )
        setClientData(clientNameList);

    } catch (e) {
        console.log(e)
    }
};

/*
@GetMapping("/getchanneltyperun/{clientid}")
@GetMapping("/getModeTypeRun/{clientid}/{channelid}")
*/

const onDisplayChannel = async (value) => {
  try {
    const channelResponse = await axios.get(`getchanneltyperun/${value}`);
    console.log(channelResponse.data)
    setLoader(false);

    const channelN = channelResponse.data;
    console.log(channelN);

    const listChannel = channelN.map((item, index) => <Option value={item.channelID} key={index} label={item.channelName}>{item.channelName}</Option>)
    setChannelData(listChannel);

  } catch (e) {
    console.log(e)
  }
};

const ongetModeType = async (value) => {
  try {
      ///alert("client id"+ClientData);
      //alert("channel id"+value);
      const modeResponse = await axios.get(`getModeTypeRun/${clientid}/${value}`);
      console.log(modeResponse.data)
     // setModeStatus(true);
      setLoader(false);

      const modeN = modeResponse.data;
      //console.log(modeN);
      //const branch = JSON.parse(modeN.branchName);
      //console.log(branch);
      const listMode = modeN.map((item, index) => <Option value={item.modeid} key={index}>{item.modename}</Option>);
      setModeData(listMode);
      
  } catch (e) {
      console.log(e)
  }
};


  const onRunRecon = async () => {
    try {
      setSpinData(true)
      const values = form.getFieldsValue();
      console.log(values)
      const importFileResponse = await axios.get(`runreconall/${clientid}/${values.fromdate}/${values.todate}/${values.channelid}/${values.modeid}`);
      console.log(importFileResponse.data)
      const runreconmsg=importFileResponse.data;
      const abc=runreconmsg.map((item,index)=>item.strMasg);
      alert("Run Recon for "+ abc);
      window.location.reload(false);

      setReconMsg(abc);
      setSpinData(false);
      setLoader(false);

      // const fileN = importFileResponse.data;
      // console.log(fileN);

      // const listFile = fileN.map((item, index) => <Option value={item.id} key={index}>{item.fileType}</Option>)
      // setData(listFile);

      //console.log(dataAll);

    } catch (e) {
      console.log(e)
    }
  };

  const menuData = props.location.state;
  console.log(menuData);

  // function onChange(value) {
  //   console.log(`selected ${value}`);
  // }
  // function handleChange(value) {
  //   console.log(`selected ${value}`);
  // }

  const [form] = Form.useForm()


  const onFileUpload = async () => {
    try {
      /*const validateFields = await form.validateFields()     
     const values = form.getFieldsValue();
     console.log(values)
    const response = await axios.get(`login1/${values.username}/${values.password}`);
     console.log(response.data)
     setMenuData(response.data)
     props.history.push("/dashboard",response.data)
      value={myStateValue || ''}
*/
     // const validateFields = await form.validateFields();    
     // const values = form.getFieldsValue();
      let currentFile=selectedFileData[0];
      const formData = new FormData();            
      formData.append('file',currentFile);
      console.log(currentFile);
      console.log(formData);
      const response = await axios.post(`importFile`,formData);
    } catch (e) {
      console.log(e)
    }
  }
 // const { RangePicker } = DatePicker;

  const dateFormat = 'DD/MM/YYYY';
 // const monthFormat = 'MM/YYYY';

//   const onChangeHandler = event => {     
//     setStateFile(event.target.files) 
// }
//   const [componentSize, setComponentSize] = useState('small');

//   const onFormLayoutChange = ({ size }) => {
//     setComponentSize(size);
//   };
//   const tailLayout = {
//     wrapperCol: { offset: 10 },
//   };
//   const FormItem = Form.Item;

//   function onChange(checkedValues) {
//     console.log('checked = ', checkedValues);
//   }

  return (
    <Layout>
      <Header style={{ padding: "20px" }}>
        <Avatar shape="square" style={{ float: "right" }} size="default" src="./max.png" />
        <Title level={3} style={{ color: "white" }}>Trace</Title>
      </Header>
      <Layout>
        <MenuSideBar menuData={menuData} />
        <Layout style={{ height: "100vh", backgroundColor: "white" }}>
          <Content>
            <Card title="Run Recon" bordered={false} style={{ width: 800 }}>
              <Form initialValues={{ remember: true }} layout={"vertical"} size={"large"} form={form}>
                <Row>
                    <Col span={8}>
                    <Form.Item label="Bank Name" name="clientName" >
                        <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeClientName}>
                              {clientData}
                           </Select>                  
                    </Form.Item>

                    </Col>
                    <Col span={8}>                      
                      <Form.Item label="Channel Type" name="channelid" >
                        <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeChannel}>
                              {channelData}
                        </Select>                  
                    </Form.Item>

                    </Col>
                    <Col span={8}>
                      <Form.Item label="Mode Type" name="modeid" >
                          <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeMode}>
                                {modeData}
                          </Select>                  
                      </Form.Item>
                    </Col>
                </Row>
                <Row>
                  <Col span={12}>
                  {/* <DatePicker  format={dateFormat} style={{width:320 }} /> */}
                  <Form.Item label="From Date" name="fromdate" style={{width:320 }}>
            {/* <DatePicker picker={"date"}  format={dateFormat}disabledTime={disabledDateTime}  style={{width:150 }} />  */}
            <Input type="date"/>
               
            </Form.Item>
                  </Col>
                  <Col span={12}>
                  <Form.Item label="To Date" name="todate" style={{width:320 }}>
            {/* <DatePicker picker={"date"}  format={dateFormat} disabledTime={disabledDateTime} style={{width:150 }} /> */}
            <Input type="date"/>
            </Form.Item>
                  </Col>
                </Row>               
                <Row>  
                  <Form.Item label=" " name="">             
                    <Button type={"primary"} size={"large"} style={{width:'100px'}} onClick={onRunRecon}>Run Recon</Button>   
                    {spindata?(<Spin style={{ margin: '0 18px', color: 'black' }} size="large" />):("") } 
                  </Form.Item>    
                </Row>
                {/* <Form.Item label={reconmsg} name=""> 
                    
                      </Form.Item>  */}
              </Form>
            </Card>
          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
};
export default RunRecon;