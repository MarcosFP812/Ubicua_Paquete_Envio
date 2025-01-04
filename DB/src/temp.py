import json
import random
from datetime import datetime, timedelta

# Función para generar datos de temperatura y humedad
def generate_temperature_humidity(start_time, end_time):
    th_data = []
    current_time = start_time
    while current_time <= end_time:
        temperature = round(random.uniform(1, 9), 2)  # Genera temperaturas que ocasionalmente sobrepasan el umbral
        humidity = round(random.uniform(30, 90), 2)   # Humedad en un rango razonable
        th_data.append({
            "Fecha": current_time.strftime('%Y-%m-%d %H:%M:%S'),
            "Temperatura": temperature,
            "Humedad": humidity
        })
        current_time += timedelta(seconds=10)
    return th_data

# Función para generar estados del ventilador
def generate_ventilator(th_data, threshold=7.0):
    ventilator_data = []
    for entry in th_data:
        temperature = entry["Temperatura"]
        ventilator_data.append({
            "Fecha": entry["Fecha"],
            "Activo": temperature >= threshold
        })
    return ventilator_data

# Función para generar estados
def generate_states(start_time, end_time, envio_time):
    state_data = []
    one_minute_before = envio_time - timedelta(minutes=1)
    few_seconds_before = envio_time - timedelta(seconds=10)

    # Antes de la entrega: estado "CARGA"
    state_data.append({
        "Fecha": one_minute_before.strftime('%Y-%m-%d %H:%M:%S'),
        "Estado": "CARGA"
    })

    # Justo antes del envío: estado "CERRAR"
    state_data.append({
        "Fecha": few_seconds_before.strftime('%Y-%m-%d %H:%M:%S'),
        "Estado": "CERRAR"
    })

    # En el momento del envío: estado "ENVIO"
    state_data.append({
        "Fecha": envio_time.strftime('%Y-%m-%d %H:%M:%S'),
        "Estado": "ENVIO"
    })

    # Al final: estado "APERTURA"
    state_data.append({
        "Fecha": end_time.strftime('%Y-%m-%d %H:%M:%S'),
        "Estado": "APERTURA"
    })

    return state_data

# Cargar los datos de ubicación desde el archivo JSON
with open('synthetic_shipments.json', 'r') as file:
    ubicaciones = json.load(file)

# Procesar cada entrada de ubicación y generar los datos
all_th_data = {}
all_ventilator_data = {}
all_state_data = {}

for key in ubicaciones:
    ubicacion = ubicaciones[key]
    start_time = datetime.strptime(ubicacion[0]["Fecha"], '%Y-%m-%d %H:%M:%S')
    end_time = datetime.strptime(ubicacion[len(ubicacion)-1]["Fecha"], '%Y-%m-%d %H:%M:%S')

    # Generar datos de temperatura y humedad
    th_data = generate_temperature_humidity(start_time, end_time)
    all_th_data[key]=th_data

    # Generar datos del ventilador
    ventilator_data = generate_ventilator(th_data)
    all_ventilator_data[key]=ventilator_data

    # Generar estados
    state_data = generate_states(start_time, end_time, start_time)
    all_state_data[key]=state_data

# Guardar los datos generados en archivos JSON
with open('temperature_humidity.json', 'w') as file:
    json.dump(all_th_data, file, indent=4)

with open('ventilator.json', 'w') as file:
    json.dump(all_ventilator_data, file, indent=4)

with open('states.json', 'w') as file:
    json.dump(all_state_data, file, indent=4)

print("Archivos generados: temperature_humidity.json, ventilator.json, states.json")

