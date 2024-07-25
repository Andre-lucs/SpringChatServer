'use strict';

import {Client} from '@stomp/stompjs'
const SocketConnectUrl = 'ws://localhost:8080/ws';
const ApiUrl = 'http://localhost:8080/api/'

let username
let client


function connect() {
    username= $("#username-input").val()
    client = new Client({
        brokerURL: SocketConnectUrl,
        onConnect: () => {
            console.log('Connected to the websocket server')

            client.subscribe(`/user/${username}/queue/messages`, (message) => {
                console.log('Received message: ', JSON.parse(message.body))
            })
            client.subscribe(`/user/${username}/queue/notifications`, (message) => {
                console.log('Received notification: ', JSON.parse(message.body))
            })
            client.subscribe(`/room/general_chat/queue/messages`, (message) => {
                console.log("new general chat message: ", JSON.parse(message.body))
            })
            client.subscribe("/room/general_chat/queue/notifications", (message) => {
                console.log("new general chat notification: ", JSON.parse(message.body))
            })
        },
        onDisconnect: () => {
            console.log('Disconnected from the websocket server')
        }
    })

    client.activate()
}

$("#connect").click(()=>connect())

$(".send").click((b)=>{
    client.publish({
        destination: "/chat/user/send",
        body: JSON.stringify({
            targetId: b.target.value,
            senderId: username,
            content: "iai cara tudo bem?"
        })
    })
})

let online = false
$("#status").click(()=>{
    online =!online
    client.publish({
        destination:"/chat/user/status",
        body:JSON.stringify({currentStatus: (online)?"ONLINE":"OFFLINE" , username:"john_doe"})
    })
})

$(".send-chat").click(b =>{
    client.publish({
        destination: "/chat/room/send",
        body: JSON.stringify({
            roomId: "general_chat",
            senderId: username,
            content: username+" mandou :oi, tudo bem?"
        })
    })
})

$("#togle-conn").click(()=>{
    if (client.active)
        client.deactivate()
    else
        client.activate()
})