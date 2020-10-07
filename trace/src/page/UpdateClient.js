import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.css';
import axios, { axiosGet } from '../utils/axios';
import MenuSideBar from './menuSideBar';
import {
    Form,
    Input,
    Button,
    Select,
    Card,
    Row,
    Col,
    Checkbox,
    Layout,
    Avatar,
    Radio,
    Table,
    Space,
} from 'antd';
import InputColor from 'react-input-color';
import Password from 'antd/lib/input/Password';
import { LayoutContext } from 'antd/lib/layout/layout';
import Title from 'antd/lib/typography/Title';
import {
    PlusCircleOutlined,
    DeleteOutlined,
} from '@ant-design/icons';
import SketchExample,{handleChange}  from '../page/SketchExample';

import { SketchPicker } from 'react-color';
const { Header, Footer, Sider, Content } = Layout;
const FormItem = Form.Item;
const { Option } = Select;
const { TextArea } = Input;


const UpdateClient = props => {
    console.log(props)
    console.log(props.record)
    const [currencyIDdata, setCurrencyID] = useState('')
    const [clientcurrency, setclientcurrency] = useState([])
    const [countrydata, setCountryData] = useState([])
    const [loader, setLoader] = useState(true)
    const [color, setColor] = React.useState({});
    const [domaindata, setDomainData] = useState([])
    const [clientdata, setClientData] = useState([])
    const [moduledata, setModuleData] = useState([])
    const [selectedFileData, setStateFile] = useState([])
    const [clientCodeData,setClientCode]=useState([])
    const [clientNameNew, setInputClientNameNew] = useState('')
    const [modelId,setModelID]=useState({})
    const [channelModeGetData,setChannelModeTableData]=useState()
    const [ChannelID,setChannelID]=useState()
    //const [modelId,setModelID]=useState({})

    console.log(channelModeGetData);
  //  console.log(channelModeGetData.CHANNELTYPE)
   // console.log(channelModeGetData.modeID)

    const recordClient = props.location.state.record;
    console.log(recordClient)
   //setInputClientNameNew(recordClient.ClientName)

    useEffect(() => {
        
        onDisplayDomainTypeList();
        onDisplayModuleTypeList();
        onDisplayClientNameList();
        onDisplaycountryTypeList();
        onGetChannelClientData();
    }, [])
    const onDisplaycountryTypeList = async () => {
        try {
            const countryTypeListResponse = await axios.get(`countryTypeList`);
            console.log(countryTypeListResponse.data)
            setLoader(false);
            const countryTypeN = countryTypeListResponse.data;
            console.log(countryTypeN);
            const listCountryType = countryTypeN.map((item, index) => <Option value={item.ID} key={index} label={item.Country}>{item.Country} </Option>)
            setCountryData(listCountryType);
        } catch (e) {
            console.log(e)
        }
    };
    const onDisplayDomainTypeList = async () => {
        try {
            const domainResponse = await axios.get(`domainTypeList`);
            console.log(domainResponse.data)
            setLoader(false);

            const domainN = domainResponse.data;
            console.log(domainN);

            //const role = JSON.parse(roleN.roleNames);
            //console.log(role);

            const listDomain = domainN.map((item, index) => <Option value={item.id} key={index}>{item.domaintype}</Option>)
            setDomainData(listDomain);

        } catch (e) {
            console.log(e)
        }
    };
    const onDisplayModuleTypeList = async () => {
        try {
            const moduleResponse = await axios.get(`moduleTypeList`);
            console.log(moduleResponse.data)
            setLoader(false);
            const moduleN = moduleResponse.data;
            console.log(moduleN);
            //const role = JSON.parse(roleN.roleNames);
            //console.log(role);

            const listModule = moduleN.map((item, index) => <Option value={item.id} key={index}>{item.moduletype}</Option>)
            setModuleData(listModule);

        } catch (e) {
            console.log(e)
        }
    };

    const onDisplayClientNameList = async () => {
        try {
            const clientNameResponse = await axios.get(`clientName`);
            console.log(clientNameResponse.data)
            setLoader(false);

            const clientNameN = clientNameResponse.data;
            console.log(clientNameN);

            //const role = JSON.parse(roleN.roleNames);
            //console.log(role);

            const clientNameList = clientNameN.map((item, index) => <Option value={item.id} key={index}>{item.clientNameList}</Option>)
            setClientData(clientNameList);

        } catch (e) {
            console.log(e)
        }
    };

    const onDisplayClientcurrency = async (checkedValues) => {
        try {
            console.log(currencyIDdata);
            const clientcurrencyResponse = await axios.get(`getclientcurrency/${checkedValues}`);
            console.log(currencyIDdata)
            console.log(clientcurrencyResponse.data)
            setLoader(false);
            const clientcurrencyN = clientcurrencyResponse.data;
            console.log(clientcurrencyN);
            const listclientcurrency = clientcurrencyN.map((item, index) => <Option value={item.CurrencyID} key={index} label={item.CurrencyCode}>{item.CurrencyCode}</Option>)
            setclientcurrency(listclientcurrency);
        } catch (e) {
            console.log(e)
        }
    };

    const onGetChannelClientData = async()  => {
        try {

            const getChannelClientDataResponse = await axios.post(`getChannelData/${recordClient.ClientID}`);
            console.log(getChannelClientDataResponse.data)
            const ChannelClientDataR=getChannelClientDataResponse.data
            const dataAll = ChannelClientDataR.map((item, index) => ({
                CHANNELTYPE:item.ChannelID,
                modeID:item.ModeID,
                clientID:item.ClientID,
                key: index,
                size: '15px'
              })
              )
              setChannelModeTableData(dataAll);
              console.log(dataAll);
        
          
        } catch (e) {
            console.log(e)
        }
    };

    const menuData = props.location.state.menuData;
    console.log(menuData);

    function onChange(value) {
        console.log(`selected ${value}`);
    }
    function handleChange(value) {
        console.log(`selected ${value}`);
    }

    const [form] = Form.useForm()
   /* const onChangeClientName = (e) => {
        setInputClientName(e.currentTarget.value);
    };*/



    const onClientUpdate = async () => {
        try {
           const validateFields = await form.validateFields();    
            const values = form.getFieldsValue();
            let currentFile=selectedFileData[0];
            const formData = new FormData();            
            formData.append('file',currentFile);
            console.log(currentFile);
            console.log(formData);
     
                                    const response = await axios.post(`clientConfigMaster/${recordClient.ClientID}/${"UPDATE"}/${values.domainid}/${values.moduleid}/${values.clientcode}/${values.clientname}/${values.address}/${values.contactno}/${values.emailid}/${values.concernperson}/${values.cpemailid}/${values.cpcontactno}/${values.isbank}/${values.isactive}/${values.countryid}/${values.currencyid}/${values.ftp_ip}/${values.ftpusername}/${values.ftppassword}/${values.ftpport}/${values.userlimit}/${values.terminalcount}/${values.reporttime}/${"ffffff"}`,formData,
                                    {
                                        headers: {
                                          "Content-Type": "multipart/form-data",
                                        }});
            console.log(response.data)

            window.location.reload(false);

            /*if(JSON.stringify(response.data) === 'Save')
            {
              alert("user added successfully");
            }
            else{
              alert("already exist");
            }*/
            //props.history.push("/AddUser",response.data)

        } catch (e) {
            console.log(e)
        }
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
        /*   setCurrencyID(checkedValues);
           onDisplayClientcurrency();
           console.log("hiiiii")*/
    }
    function onChange(checkedValues) {
        console.log('checked = ', checkedValues);
        /*   setCurrencyID(checkedValues);
           onDisplayClientcurrency();
           console.log("hiiiii")*/
    }

    function onChangeCurrency(checkedValues) {
        console.log(`selected ${checkedValues}`);
        console.log(checkedValues);
        setCurrencyID(checkedValues);
        onDisplayClientcurrency(checkedValues);
    }
    function onChangeChannel(checkedValues) {
        console.log(`selected ${checkedValues}`);
        console.log(checkedValues);
        setChannelID(checkedValues);
       // onDisplayClientcurrency(checkedValues);
    }

    function onChangeMode(checkedValues) {
        console.log('checked = ', checkedValues);
        setModelID(checkedValues);
      }
   const dataSource = [
        {
            key: '1',
            CHANNELTYPE: <Select style={{width:'45%'}} defaultValue={"--select--"} onChange={onChangeChannel}></Select>,
            ONUS:  <Checkbox.Group onChange={onChangeMode}><Checkbox value={1}></Checkbox></Checkbox.Group>,
            ACQUIRER: <Checkbox.Group onChange={onChangeMode}><Checkbox value={2}></Checkbox></Checkbox.Group>,
            ISSUER: <Checkbox.Group onChange={onChangeMode}><Checkbox value={3}></Checkbox></Checkbox.Group>,
            MORE: <Space size="large">
                <a><PlusCircleOutlined /></a>
                <a><DeleteOutlined /></a>
            </Space>,
        },

    ];
    
    const columns = [
        {
            title: 'CHANNEL TYPE',
            dataIndex: 'CHANNELTYPE',
            key: 'CHANNELTYPE',
            render: (CHANNELTYPE) => (
                <Space size="middle">
                 
                  <Select style={{width:'45%'}} defaultValue={"--select--"} onChange={onChangeChannel}>{CHANNELTYPE}</Select>
          
                </Space>),
        },
        {
            title: 'ONUS',
            dataIndex: 'modeID',
            key: 'ONUS',
            render: (modeID) => (
                <Space size="middle">
                 
                 <Checkbox.Group onChange={onChangeMode}><Checkbox value={modeID}></Checkbox></Checkbox.Group>,
          
                </Space>),
        },
        {
            title: 'ACQUIRER',
            dataIndex: 'modeID',
            key: 'ACQUIRER',
        },
        {
            title: 'ISSUER',
            dataIndex: 'modeID',
            key: 'ISSUER',
        },
        {
            title: 'MORE',
            dataIndex: 'MORE',
            key: 'MORE',
        },
    ];
  const fileChangedHandler = event => {
      
        setStateFile(event.target.files)
       
    }
    const onColor = async() =>{
        const abc=props.colorHex
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
                                <Card title="Client Resgistraion" bordered={false} style={{ width: 1000 }} >
                                
                                    <Form initialValues={{ 
                                                  clientname: recordClient.ClientName,
                                                  clientcode: recordClient.ClientCode,
                                                  address: recordClient.ClientAddress,
                                                  domainid: recordClient.DomainID,
                                                  moduleid: recordClient.ModuleID,
                                                  address: recordClient.ClientAddress,
                                                  contactno: recordClient.ContactNo,
                                                  emailid: recordClient.EmailID,
                                                  concernperson: recordClient.ConcernPersonName,
                                                  cpemailid: recordClient.ConcernPersonEmailID,
                                                  cpcontactno: recordClient.ConcernPersonContactNo,
                                                  isbank: recordClient.IsBank,
                                                  isactive: recordClient.IsActive,
                                                  countryid: recordClient.CounrtyID,
                                                  currencyid: recordClient.CurrencyID,
                                                  ftp_ip: recordClient.FTPIP,
                                                  ftpusername: recordClient.FTPUsername,
                                                  ftpport: recordClient.FTPPort,
                                                  userlimit: recordClient.UserLimit,
                                                  terminalcount: recordClient.TerminalCount,
                                                  reporttime: recordClient.ReportCutOffTime,
                                                  colorcode:recordClient.ColorCode,
                                                 }} 
                                    layout={"vertical"} form={form}>
                                        <b><h2>Basic Information</h2></b>
                                        <Row gutter={8} layout="inline">
                                            <Col flex={4}><b>
                                                <Form.Item
                                                    label="Client Name"
                                                    name="clientname"
   
                                                    rules={[{ required: true, message: 'Please input your Client name!' }]}>
                                                    <Input size={"large"} placeholder="Enter Client Name" 
                                                    />
                                                </Form.Item>
                                            </b></Col>
                                            <Col flex={1}><b>
                                                <Form.Item
                                                    label="Client Code"
                                                    name="clientcode"
                                                    rules={[{ required: true, message: 'Please input your Client Code!' }]}>
                                                    <Input size={"large"} placeholder="Enter Client Code" defaultValue={clientCodeData} />
                                                </Form.Item>
                                            </b></Col>
                                        </Row>
                                        <Row gutter={8}>
                                            <Col flex={10}><b>
                                                <Form.Item
                                                    label="Client Address"
                                                    name="address"
                                                    rules={[{ required: true, message: 'Please input your Client Address!' }]}>
                                                    <TextArea rows={2} />
                                                </Form.Item>
                                            </b></Col>
                                        </Row>
                                        <Row gutter={8} layout="inline">
                                            <Col flex={2}><b>
                                                <Form.Item
                                                    label="Contact no"
                                                    name='contactno'
                                                    rules={[{ required: true, message: 'Please input your Contact No!' }]}>
                                                    <Input size={"large"} placeholder="Enter Contact No" />
                                                </Form.Item>
                                            </b></Col>
                                            <Col flex={2}><b>
                                                <Form.Item
                                                    label="Email Id"
                                                    name="emailid"
                                                    rules={[{ required: true, message: 'Please input your Email ID!' }]}>
                                                    <Input size={"large"} placeholder="Enter Email ID" />
                                                </Form.Item>
                                            </b></Col>
                                        </Row>
                                        <Row gutter={8} layout="inline">
                                            <Col flex={2}><b>
                                                <Form.Item
                                                    label="Concern Person Name"
                                                    name="concernperson"
                                                >
                                                    <Input size={"large"} placeholder="Enter Concern Person Name" />
                                                </Form.Item>
                                            </b></Col>
                                            <Col flex={2}><b>
                                                <Form.Item
                                                    label="Concern Person Contact No"
                                                    name="cpcontactno"
                                                >
                                                    <Input size={"large"} placeholder="Enter Concern Person Contact No" />
                                                </Form.Item>
                                            </b></Col>
                                            <Col flex={2}><b>
                                                <Form.Item
                                                    label="Concern Person Email ID"
                                                    name="cpemailid"
                                                >
                                                    <Input size={"large"} placeholder="Enter Concern Person Email ID" />
                                                </Form.Item>
                                            </b></Col>
                                        </Row>
                                        <b><h2>FTP Credential</h2></b>
                                        <Row gutter={8} layout="inline">
                                            <Col flex={2}><b>
                                                <Form.Item
                                                    label="FTP Username"
                                                    name="ftpusername"
                                                    rules={[{ required: true, message: 'Please input your FTP Username !' }]}>
                                                    <Input size={"large"} placeholder="Enter FTP Username" />
                                                </Form.Item>

                                            </b></Col>
                                            <Col flex={2}><b>
                                                <Form.Item
                                                    label="FTP IP "
                                                    name="ftp_ip"
                                                    rules={[{ required: true, message: 'Please input your FTP IP !' }]}>
                                                    <Input size={"large"} placeholder="Enter FTP IP " />
                                                </Form.Item>
                                            </b></Col>
                                        </Row>
                                        <Row gutter={8} layout="inline">
                                            <Col flex={2}><b>
                                                <Form.Item
                                                    label="FTP Password "
                                                    name="ftppassword"
                                                    rules={[{ required: true, message: 'Please input your FTP Password !' }]}>
                                                    <Password size={"large"} placeholder="Enter FTP Password" />
                                                </Form.Item>

                                            </b></Col>
                                            <Col flex={2}><b>
                                                <Form.Item
                                                    label="FTP Port "
                                                    name="ftpport"
                                                    rules={[{ required: true, message: 'Please input your FTP Port !' }]}>
                                                    <Input size={"large"} placeholder="Enter FTP Port " />
                                                </Form.Item>
                                            </b></Col>
                                        </Row>

                                        <b><h2>Other Details</h2></b>
                                        <Row gutter={8} layout="inline">
                                            <Col flex={2}><b>
                                                <Form.Item
                                                    label="Country Code "
                                                    name="countryid"
                                                    rules={[{ required: true, message: 'Please input your Country Code !' }]}>
                                                    <Select defaultValue="lucy" style={{ width: 200 }} onChange={onChangeCurrency} >
                                                        {countrydata}
                                                    </Select>
                                                </Form.Item>
                                            </b></Col>
                                            <Col flex={2}><b>
                                                <Form.Item
                                                    label="Currency Code "
                                                    name="currencyid"
                                                    rules={[{ required: true, message: 'Please input your Currency Code !' }]}>
                                                    <Select defaultValue="lucy" style={{ width: 200 }} onChange={onChange}>
                                                        {clientcurrency}
                                                    </Select>
                                                </Form.Item>
                                            </b></Col>
                                            <Col flex={2}><b>
                                                <Form.Item
                                                    label="Domain Type"
                                                    name="domainid"
                                                    rules={[{ required: true, message: 'Please input your Domain Type !' }]}>
                                                    <Select defaultValue="lucy" onChange={onChange}>
                                                        {domaindata}
                                                    </Select>
                                                </Form.Item>
                                            </b></Col>
                                        </Row>
                                        <Row gutter={8} layout="inline">
                                            <Col flex={3}><b>
                                                <Form.Item
                                                    label="Module Type  "
                                                    name="moduleid"
                                                    rules={[{ required: true, message: 'Please input your Module Type !' }]}
                                                >
                                                    <Select style={{ width: 200 }} onChange={onChange}>
                                                        {moduledata}
                                                    </Select>
                                                </Form.Item>
                                            </b></Col>
                                            <Col flex={3}><b>
                                                <Form.Item
                                                    label="Terminal Count"
                                                    name="terminalcount"
                                                >
                                                    <Input size={"large"} />
                                                </Form.Item>
                                            </b></Col>
                                            <Col flex={3}><b>
                                                <Form.Item
                                                    label="User Limit"
                                                    name="userlimit"
                                                >
                                                    <Input size={"large"} />
                                                </Form.Item>
                                            </b></Col>
                                        </Row>
                                        <Row gutter={8} layout="inline">
                                            <Col flex={1}><b>
                                                <Form.Item
                                                    label="Report Cut-Off Time"
                                                    name="reporttime"
                                                    rules={[{ required: true, message: 'Please input your Report Cut-Off Time!' }]}>
                                                    <Input size={"large"} placeholder="Enter Report Cut-Off Time" />
                                                </Form.Item>
                                            </b></Col>
                                            <Col flex={4}><b>
                                                <Form.Item
                                                    label="Logo"
                                                    name="file"
                                                    rules={[{ required: true, message: 'Please input your Logo !' }]}>
                                                    <Input type={'file'} name="file" size={"large"} placeholder="Enter Logo "  onChange={fileChangedHandler} />
                                                </Form.Item>
                                            </b></Col>
                                        </Row>
                                        <Row gutter={8} layout="inline">
                                            <Col flex={1}><b>
                                                <Form.Item
                                                    label="Choose Color Code"
                                                    name="colorcode"
                                                >
                                                   <Input/>
                                                </Form.Item>
                                            </b></Col>
                                        </Row>
                                        <Row gutter={8} layout="inline">
                                            <Col flex={1}><b>
                                                <Form.Item
                                                    label="Is Bank"
                                                    name="isbank"
                                                    rules={[{ required: true, message: 'Please input your Choose Color Code!' }]}
                                                >
                                                    <Radio.Group name="radiogroup" >
                                                        <Radio value={1}>Yes</Radio>
                                                        <Radio value={2}>No</Radio>
                                                    </Radio.Group>
                                                </Form.Item>
                                            </b></Col>
                                            <Col flex={1}><b>
                                                <Form.Item
                                                    label="Is Active"
                                                    name="isactive"
                                                    rules={[{ required: true, message: 'Please input your Choose Color Code!' }]}>
                                                    <Radio.Group name="radiogroup" >
                                                        <Radio value={1}>Yes</Radio>
                                                        <Radio value={2}>No</Radio>
                                                    </Radio.Group>
                                                </Form.Item>
                                            </b></Col>
                                        </Row>
                                        <Table columns={columns} dataSource={dataSource} />
                                        <Row>
                                            <Form.Item>
                                                <Button type="primary" onClick={onClientUpdate}>Update</Button>
                                                <Button style={{ margin: '0 8px', color: 'black' }} onClick={props.history.goBack} >Back</Button>
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
        export default UpdateClient;
