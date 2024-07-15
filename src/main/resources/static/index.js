import {Client} from '@stomp/stompjs'
const SocketConnectUrl = 'ws://localhost:8080/chatapp';
const ApiUrl = 'http://localhost:8080/api/'

let clients = {};
function updateMessages(body, id) {
    console.log('chatid: ',id)
    console.log('content: ',body.content)
    let messages = $(`#chat${id} .chat-messages`)
    messages.append(`<div class="message">${body.sender}: ${body.content}</div>`)
}

function connectChatRoom(id, roomName) {
    if (clients[id]) {
        disconnectChatRoom(id);
    }
    clients[id] = createClient(id, roomName)
    clients[id].client.activate();
}

function disconnectChatRoom(id) {
    clients[id].client.deactivate();
    delete clients[id];
}

function createClient(number, roomName = 'general') {
    let client = new Client({
        brokerURL: SocketConnectUrl,
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
    return {client:client, roomName:roomName};

}

export function sendMessage(id){
    let msgcontent = $('#input'+id)[0].value
    console.log(JSON.stringify({
        sender: 'client'+id,
        content:msgcontent,
        date: new Date()
    }))
    clients[id].client.publish({
        destination: `/chat/send/${clients[id].roomName}`,
        body: JSON.stringify({
            sender: 'client'+id,
            content:msgcontent,
            date: new Date()
        }),
        headers: {
            'Content-Type': 'application/json'
        }
    })
}


async function loadRooms() {
    let current_rooms = await (await fetch(`${ApiUrl}chatrooms`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })).json()

    $( ".chat-room-select" ).empty()
    $( ".room-list" ).empty()
    current_rooms.forEach(room => {
        $( ".chat-room-select" ).append(
            $('<option></option>').text(room.name).val(room.name)
        )
        $(".room-list").append(
            $('<li></li>').text(room.name)
        )
    })
}

function createRoom($input) {
    let value = $input.val()
    if(value.trim().length < 5) {
        alert("Room name should be at least 5 characters long")
        return;
    }
    fetch(`${ApiUrl}chatrooms`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({name: value, active: true})
    }).then(r => loadRooms()).catch(err => console.log(err))
    $input.val("")
}

$(function () {
    $( ".send-btn" ).click((b) => sendMessage(b.target.value));
    $( ".chat-connect").click(b => connectChatRoom(b.target.value, $( `#chat${b.target.value} .chat-room-select`).value))
    $( ".chat-disconnect").click(b => disconnectChatRoom(b.target.value))
    $("#create-room-btn").click(b => createRoom($("input#new-room-name")))
    loadRooms();


});