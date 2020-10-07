import React,{useState} from 'react';
import { Layout , Form, Input, Button} from 'antd';
import axios from '../utils/axios';
import MenuContext from '../utils/MenuContext';


const Login = props => {

    const [form] = Form.useForm()
    const [menuData,setMenuData] = useState(null)
    
    const onLogin = async()=> {

        try{
            const validateFields = await form.validateFields()     
            const values = form.getFieldsValue();
            console.log(values)
            const response = await axios.get(`login1/${values.username}/${values.password}`);
            console.log(response.data)
            setMenuData(response.data)
            props.history.push("/dashboard",response.data)
        }catch(e) {
            console.log(e)
        }
        
    }
       return(<MenuContext.Provider value={menuData}>
           <Layout style={{height:"100%",width:"100%"}}>
        <Layout.Header style={{fontSize : '50px',color :'#e6f7ff',fontFamily :'Segoe UI'}}>
            Login
        </Layout.Header> 


        <Layout.Content>

            <div style={{height:"25%",width:"25%"}}>

                <Form form={form}>

                    <Form.Item name="username" rules={[{required:true,message:"Username required"}]} >
                        <Input />
                    </Form.Item>

                    <Form.Item name="password" rules={[{required:true,message:"Password required"}]}>
                        <Input.Password />
                    </Form.Item>

                    <Form.Item>
                        <Button onClick={onLogin}>Login</Button>
                    </Form.Item>

                </Form>
            </div>
        </Layout.Content>

        <Layout.Footer>
            Footer
        </Layout.Footer>

    </Layout></MenuContext.Provider>)
}

export default Login;