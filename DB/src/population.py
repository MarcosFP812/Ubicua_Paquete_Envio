import json
import random
import datetime
from xml.etree import ElementTree as ET
import requests
from tqdm import tqdm

def extract_coordinates_from_kml(file_path):
    """Extrae las coordenadas del archivo KML."""
    with open(file_path, 'r', encoding='utf-8') as file:
        kml_content = file.read()

    tree = ET.fromstring(kml_content)
    namespace = {'ns': 'http://www.opengis.net/kml/2.2'}
    coordinates = []

    for placemark in tree.findall('.//ns:Placemark', namespace):
        for coord in placemark.findall('.//ns:coordinates', namespace):
            coords_text = coord.text.strip()
            coords_list = coords_text.split()
            for pair in coords_list:
                lon, lat, _ = pair.split(",")
                coordinates.append((float(lon), float(lat)))

    return coordinates

def get_speed_limit(lon, lat, last_speed=None):
    """Consulta el límite de velocidad usando un servicio de mapas gratuito."""
    try:
        query = f"""
        [out:json];
        way
        (around:50,{lat},{lon})["highway"];
        out tags;
        """

        url = "https://overpass-api.de/api/interpreter"

        response = requests.get(url, params={'data': query})

        if response.status_code == 200:
            data = response.json()

            # Buscar etiquetas con 'maxspeed'
            for element in data.get('elements', []):
                if 'tags' in element and 'maxspeed' in element['tags']:
                    max_vel = element['tags']['maxspeed']
                    #print(f"\nVelocidad máxima encontrada: {max_vel}\n")
                    return float(max_vel)
        
        # Si no se encuentra información, usar velocidad previa o un valor predeterminado
        if last_speed is not None:
            #print(f"Usando velocidad previa: {last_speed}")
            return last_speed

        #print("No se ha encontrado velocidad en la vía, seleccionando un valor por defecto.")
        return random.choice([30, 50, 70, 90, 120])  # Velocidades predeterminadas
    except Exception as e:
        print(f"Error al consultar límite de velocidad: {e}")
        
        # Manejar errores devolviendo la última velocidad o un valor predeterminado
        if last_speed is not None:
            print(f"Usando velocidad previa debido a error: {last_speed}")
            return last_speed
        
        print("No se ha encontrado velocidad en la vía, seleccionando un valor por defecto.")
        return random.choice([30, 50, 70, 90, 120])

def calculate_time_increment(distance_km, speed_kmh):
    """Calcula el tiempo necesario para recorrer una distancia a una velocidad dada."""
    if speed_kmh > 0:
        return datetime.timedelta(hours=(distance_km / speed_kmh))
    return datetime.timedelta(seconds=0)

def generate_shipment_data(lon, lat, last_speed, current_time, last_coords):
    """Genera datos sintéticos para una coordenada."""
    # Generar ligeras variaciones en la longitud y latitud
    synthetic_lon = lon + random.uniform(-0.0001, 0.0001)
    synthetic_lat = lat + random.uniform(-0.0001, 0.0001)

    # Consultar límite de velocidad basado en la ubicación
    base_speed = get_speed_limit(lon, lat, last_speed)

    random_choice = random.random()  # Genera un número entre 0 y 1

    if random_choice <= 0.01:
        # 1% de probabilidad de ser base_speed + 20 km/h
        speed = base_speed + 20
    elif random_choice <= 0.95:
        # 94% de probabilidad de estar entre base_speed +/- 5 km/h
        speed = base_speed + random.uniform(-5, 5)
    else:
        # 5% de probabilidad de ser exactamente base_speed
        speed = base_speed
    
    if last_speed:
        speed = (speed + last_speed) / 2

    # Calcular distancia desde la última coordenada
    if last_coords:
        last_lon, last_lat = last_coords
        distance_km = ((synthetic_lon - last_lon)**2 + (synthetic_lat - last_lat)**2)**0.5 * 111  # Aproximación de distancia
        time_increment = calculate_time_increment(distance_km, speed)
        current_time += time_increment

    return {
        "Longitud": synthetic_lon,
        "Latitud": synthetic_lat,
        "Velocidad": speed,
        "Fecha": current_time.strftime('%Y-%m-%d %H:%M:%S')
    }, current_time

def is_weekday(date):
    """Verifica si una fecha es un día laborable."""
    return date.weekday() < 5

def generate_synthetic_data(coordinates, num_shipments, start_time):
    """Genera datos sintéticos para los envíos sin multiprocessing."""
    r = {}
    last_speed = None
    current_time = start_time
    last_coords = None

    for i in tqdm(range(num_shipments), desc="Generando envíos", leave=True):
        # Asegurarse de que current_time sea un día laborable
        while not is_weekday(current_time):
            current_time += datetime.timedelta(days=1)
        shipments = []
        for lon, lat in tqdm(coordinates, desc="Procesando coordenadas", leave=False):
            shipment, current_time = generate_shipment_data(lon, lat, last_speed, current_time, last_coords)
            
            shipments.append(shipment)
            last_speed = shipment["Velocidad"]  # Actualizar la última velocidad válida
            last_coords = (lon, lat)  # Actualizar las últimas coordenadas

        # Incrementar el tiempo al siguiente día laborable a la misma hora
        current_time += datetime.timedelta(days=1)
        while not is_weekday(current_time):
            current_time += datetime.timedelta(days=1)

        r[i] = shipments

        current_time = current_time.replace(hour=start_time.hour, minute=start_time.minute, second=start_time.second)

    return r

def write_to_json(data, output_path):
    """Escribe los datos en un archivo JSON."""
    with open(output_path, 'w', encoding='utf-8') as file:
        json.dump(data, file, ensure_ascii=False, indent=4)

# Ruta del archivo KML
input_kml_path = 'guada1.kml'

# Número de envíos a generar
num_shipments = 30

# Fecha y hora de inicio
start_time = datetime.datetime(2024, 10, 28, 10, 0)

# Extraer coordenadas
coordinates = extract_coordinates_from_kml(input_kml_path)

# Generar datos sintéticos
synthetic_shipments = generate_synthetic_data(coordinates, num_shipments, start_time)

# Guardar los datos en un archivo JSON
output_json_path = 'synthetic_shipments_t1_guada.json'
write_to_json(synthetic_shipments, output_json_path)

print(f"Datos sintéticos generados y guardados en {output_json_path}")



