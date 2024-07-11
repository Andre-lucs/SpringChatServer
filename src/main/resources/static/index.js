import {Client} from '@stomp/stompjs'
const connectUrl = 'ws://localhost:8080/chatapp';

function updateMessages(body, id) {
    console.log('chatid: ',id)
    console.log('content: ',body.content)
    let messages = $(`#chat${id} .chat-messages`)
    messages.append(`<div class="message">${body.sender}: ${body.content}</div>`)
}

function createClient(number, roomName = 'general') {
    let client = new Client({
        brokerURL: connectUrl,
        onConnect: (frame) => {
            console.log('Connected to WebSocket server')
            client.subscribe(`/room/${roomName}`, (message) => {
                updateMessages(JSON.parse(message.body), number);
            })
        },

        onWebSocketError: (error) => {
            console.error('WebSocket error:', error)
        },

        onStompError: (error) => {
            console.error('Stomp error:', error)
        },
        onDisconnect: frame => {
            console.log('Disconnected from WebSocket server: ', frame)
            //setTimeout(() => clients[number].connect(), 5000) // reconnect after 5 seconds
        }
    })
    client.activate()
    return client;

}

let clients = {
    '1': createClient(1),
    '2': createClient(2),
    '3': createClient(3)
}

export function sendMessage(id){
    let msgcontent = $('#input'+id)[0].value
    console.log(JSON.stringify({
        sender: 'client'+id,
        content:msgcontent,
        date: new Date()
    }))
    clients[id].publish({
        destination: '/chat/send/general',
        body: JSON.stringify({
            sender: 'client'+id,
            content:msgcontent,
            date: new Date()
        })
    })
}

$(function () {
    $( ".send-btn" ).click((b) => sendMessage(b.target.value));
});