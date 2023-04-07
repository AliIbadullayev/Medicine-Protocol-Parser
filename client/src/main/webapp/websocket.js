let socket = new WebSocket("ws://localhost:8080/alibaba/websocket");

socket.onmessage = function(event) {
    var json = JSON.parse(event.data);
    document.getElementById("snils").innerHTML = json.patient.snils;
    document.getElementById("name").innerHTML = json.patient.name;
    document.getElementById("surname").innerHTML = json.patient.surname;
    document.getElementById("birth").innerHTML = json.patient.birth;

    document.getElementById("ecg").innerHTML = json.observation.ecg;
    document.getElementById("pulse").innerHTML = json.observation.pulse;
    document.getElementById("sat").innerHTML = json.observation.sat;
    document.getElementById("co2_insp").innerHTML = json.observation.co2_insp;
    document.getElementById("co2_exp").innerHTML = json.observation.co2_exp;
    document.getElementById("observation_time").innerHTML = json.observation.observation_time;
};

