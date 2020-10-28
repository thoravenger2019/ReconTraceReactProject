import React, { useState } from 'react';
import { Layout, Form, Input, Button, Row, Col, Card } from 'antd';
import axios from '../utils/axios';
import MenuContext from '../utils/MenuContext';


const Login = props => {

    const [form] = Form.useForm()
    const [menuData, setMenuData] = useState(null)

    const onLogin = async () => {

        try {
            const validateFields = await form.validateFields()
            const values = form.getFieldsValue();
            console.log(values)
            const response = await axios.get(`login1/${values.username}/${values.password}`);
            console.log(response.data)
            setMenuData(response.data)
            props.history.push("/dashboard", response.data)
        } catch (e) {
            console.log(e);
        }

    }
    return (<MenuContext.Provider value={menuData}>
        <Layout style={{ height: "100%", width: "100%" }}>
            <Layout.Header style={{ fontSize: '50px', color: '#e6f7ff', fontFamily: 'Segoe UI',textAlign: 'center' }}>
                Login
        </Layout.Header>


            <Layout.Content>

                <Row>
                    <Col md={{ span: 12, offset: 6 }}>
                        <Card style={{ textAlign: 'center' }}>
                            <Form form={form}>

                                <Form.Item name="username" rules={[{ required: true, message: "Username required" }]} >
                                    <Input size="large" placeholder="Username" />
                                </Form.Item>

                                <Form.Item name="password" rules={[{ required: true, message: "Password required" }]}>
                                    <Input.Password size="large" placeholder="Password"/>
                                </Form.Item>

                                <Form.Item>
                                    <Button size="large"  onClick={onLogin}>Login</Button>
                                </Form.Item>

                            </Form>

                        </Card>
                    </Col>
                </Row>
            </Layout.Content>

            <Layout.Footer>
                Footer
        </Layout.Footer>

        </Layout></MenuContext.Provider>)
}

export default Login;