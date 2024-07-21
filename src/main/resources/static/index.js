import {Client, Stomp} from '@stomp/stompjs'
const SocketConnectUrl = 'ws://localhost:8080/chatapp';
const ApiUrl = 'http://localhost:8080/api/'

let windows = {}

function updateMessages(body, id) {
    console.log('chatid: ',id)
    console.log('content: ',body.content)
    let messages = $(`#chat${id} .chat-messages`)
    messages.append(`<div class="message">${body.sender}: ${body.content}</div>`)
}

function connectChatRoom(id) {
    let roomName = $( `#chat${id} .chat-room-select`).val()
    let username = $(`#chat${id} .username-input`).val()
    console.log("connecnt to ", roomName)
    if (windows[id].client) {
        disconnectChatRoom(id);
    }
    windows[id].client = createClient(id, roomName, username)
    windows[id].client.client.activate();

    $(`#chat${id} .chat-messages`).empty()
}

function disconnectChatRoom(id) {
    console.log("unsub "+id)
    console.log(windows)
    windows[id].client.client.unsubscribe();
    windows[id].client.client.deactivate(true)
    delete windows[id].client;
    console.log(windows)
}

function createClient(number, roomName = 'general',username) {

    let client = new Client({
        brokerURL: SocketConnectUrl,
        onConnect: (frame) => {
            console.log('Connected to WebSocket server'+ frame.body)

            client.subscribe(`/room/${roomName}`, (message) => {
                if(windows[number].client.client.id == null){
                    windows[number].client.client.id = message.headers["message-id"].toString().substring(0, message.headers["message-id"].toString().lastIndexOf('-'))
                    console.log("specific user id :",windows[number].client.client.id)
                    client.subscribe(`/user/queue/specific-user/${windows[number].client.client.id}`, (message)=>{
                        console.log(message.body)
                    })
                }
                updateMessages(JSON.parse(message.body), number);
            })
        },
        onUnhandledMessage: (msg) => {
            console.error('Unhandled message:', msg)
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
        },
        onUnhandledFrame: (frame) => {
            console.log(frame)
        }

    })
    return {client:client, roomName:roomName, username:username, id: null};

}

export function sendMessage(id){
    let msgcontent = $('#input'+id)[0].value

    windows[id].client.client.publish({
        destination: `/chat/send/${windows[id].client.roomName}`,
        body: JSON.stringify({
            sender: windows[id].client.username,
            content:msgcontent,
            date: new Date()
        }),
        headers: {
            'Content-Type': 'application/json'
        }
    })
}


async function loadRooms() {
    let current_rooms = await (await fetch(`${ApiUrl}chatrooms/active`, {
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
        console.log(room.lastMessage != null)
        $(".room-list").append(
            $('<li></li>').text(room.name+" | "+room.activeUsers+" | ").append(
                (room.lastMessage != null) ? ( room.lastMessage?.sender+": "+ room.lastMessage?.content) : ("")
            )
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
function createChatWindow(id) {
    const chatWindow = $("<div class=\"chat-window\" id=\"chat1\">\n" +
        "            <div class=\"chat-header\">\n" +
        "                <h4>\n" +
        "                    <span>Room:</span>\n" +
        "                    <select class=\"chat-room-select\">\n" +
        "                        <option value=\"lolol\">lolol</option>\n" +
        "                    </select>\n" +
        "                    <label>\n" +
        "                        <span>User: </span>\n" +
        "                        <input type=\"text\" class=\"username-input\" placeholder=\"Your username\">\n" +
        "                    </label>\n" +
        "                    <button value=\"1\" class=\"chat-connect\">Connect</button>\n" +
        "                </h4>\n" +
        "                <button value=\"1\" class=\"chat-quit\">X</button>\n" +
        "            </div>\n" +
        "            <div class=\"chat-messages\">\n" +
        "                <!-- Messages will be displayed here -->\n" +
        "            </div>\n" +
        "            <div class=\"chat-input\">\n" +
        "                <input type=\"text\" id=\"input\" placeholder=\"Type a message...\">\n" +
        "                <button value=\"1\" class=\"send-btn\">Send</button>\n" +
        "            </div>\n" +
        "        </div>");
    chatWindow.attr('id', `chat${id}`);
    chatWindow.find(".chat-connect").attr('value', id)
    chatWindow.find(".chat-quit").first().attr('value', id)
    chatWindow.find(".send-btn").first().attr('value', id)
    chatWindow.find("#input").attr('value', id)
    chatWindow.find("#input").attr('id',"input"+id)
    $('body').append(chatWindow);

    // Add event listeners
    $(`.chat-connect[value=${id}]`).click(function () {
        connectChatRoom(id);
    });

    $(`.chat-quit[value=${id}]`).click(function () {
        disconnectChatRoom(id);
    });

    $(`.send-btn[value=${id}]`).click(function (b) {
        sendMessage(id);
    });

    $(".chat-container").first().append(chatWindow)

    windows[id] = {window:chatWindow, client:null}

    loadRooms()
    setupListeners()
}
$(function () {
    setupListeners()
    loadRooms();
});

function setupListeners(){
    $( ".chat-connect").off('click').click(b => connectChatRoom(b.target.value))
    $( ".send-btn" ).off('click').click((b) => sendMessage(b.target.value));
    $( ".chat-quit").off('click').click(b => disconnectChatRoom(b.target.value))
    $("#create-room-btn").off('click').click(b => createRoom($("input#new-room-name")))
    $("#reload-rooms").off('click').click(b => loadRooms())
    $("#add-chat-btn").off('click').click(b => createChatWindow(Object.keys(windows).length))
}