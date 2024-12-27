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
        print(placemark)
        for coord in placemark.findall('.//ns:coordinates', namespace):
            print(coord)
            coords_text = coord.text.strip()
            coords_list = coords_text.split()
            for pair in coords_list:
                print(pair)
                lon, lat, _ = pair.split(",")
                coordinates.append((float(lon), float(lat)))

    return coordinates

def get_speed_limit(lon, lat, last_speed=None):
    """Consulta el límite de velocidad usando un servicio de mapas gratuito."""
    try:
        response = requests.get(
            f"https://router.project-osrm.org/route/v1/driving/{lon},{lat};{lon+0.01},{lat+0.01}",
            params={"overview": "false"}
        )
        if response.status_code == 200:
            data = response.json()
            # Extraer un valor aproximado de velocidad máxima
            speed_limits = [annotation.get("maxspeed", None) for annotation in data.get("routes", [])]
            # Filtrar velocidades válidas
            valid_speeds = [int(speed) for speed in speed_limits if speed and speed.isdigit()]
            if valid_speeds:
                return max(valid_speeds)
        if last_speed is not None:
            return last_speed  # Usar la velocidad previa si no hay datos nuevos
        return random.choice([30, 50, 70, 90, 120])  # Valor por defecto si no hay datos y no hay velocidad previa
    except Exception as e:
        print(f"Error al consultar límite de velocidad: {e}")
        if last_speed is not None:
            return last_speed  # Usar la velocidad previa si ocurre un error
        return random.choice([30, 50, 70, 90, 120])

def generate_shipment_data(lon, lat, last_speed):
    """Genera datos sintéticos para una coordenada."""
    # Generar ligeras variaciones en la longitud y latitud
    synthetic_lon = lon + random.uniform(-0.0005, 0.0005)
    synthetic_lat = lat + random.uniform(-0.0005, 0.0005)

    # Consultar límite de velocidad basado en la ubicación
    base_speed = get_speed_limit(synthetic_lon, synthetic_lat, last_speed)
    speed = base_speed + (20 if random.random() < 0.1 else 0)  # 10% de probabilidad de ir 20 km/h más rápido

    # Generar fecha y hora aleatoria
    now = datetime.datetime.now()
    time_delta = datetime.timedelta(seconds=random.randint(0, 3600))
    timestamp = now + time_delta

    return {
        "Longitud": synthetic_lon,
        "Latitud": synthetic_lat,
        "Velocidad": speed,
        "Fecha": timestamp.isoformat()
    }

def generate_synthetic_data(coordinates, num_shipments):
    """Genera datos sintéticos para los envíos sin multiprocessing."""
    shipments = []
    last_speed = None

    for _ in tqdm(range(num_shipments)):
        for lon, lat in coordinates:
            shipment = generate_shipment_data(lon, lat, last_speed)
            shipments.append(shipment)
            last_speed = shipment["Velocidad"]  # Actualizar la última velocidad válida

    return shipments

def write_to_json(data, output_path):
    """Escribe los datos en un archivo JSON."""
    with open(output_path, 'w', encoding='utf-8') as file:
        json.dump(data, file, ensure_ascii=False, indent=4)

# Ruta del archivo KML
input_kml_path = 'doc.kml'

# Número de envíos a generar
num_shipments = 1

# Extraer coordenadas
coordinates = extract_coordinates_from_kml(input_kml_path)

# Generar datos sintéticos
synthetic_shipments = generate_synthetic_data(coordinates, num_shipments)

# Guardar los datos en un archivo JSON
output_json_path = 'synthetic_shipments.json'
write_to_json(synthetic_shipments, output_json_path)

print(f"Datos sintéticos generados y guardados en {output_json_path}")

