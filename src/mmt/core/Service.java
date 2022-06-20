package mmt.core;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.lang.StringBuilder;
import java.time.Duration;

public class Service implements java.io.Serializable {

	private int _id;
	private double _cost;
	private Departure _iniDep;
	private Departure _lastDep;
	private List<Departure> _depList;
	private Duration _totalTime;

	private static Comparator<Service> _compareById = new Comparator<Service>() { // Sorts services by id
		public int compare(Service s1, Service s2) {
			return s1.getId() - s2.getId();
		}
	};


	Service(int id, double cost) {
		_id = id;
		_cost = cost;
		_depList = new ArrayList<>();
		_totalTime = Duration.ZERO;
	}


	static Comparator<Service> getCompareById(){
		return _compareById;
	}

	Duration getTotalTime(){
		return _totalTime;
	}

	double getCost() {
		return _cost;
	}

	int getId() {
		return _id;
	}

	Departure getIniDeparture() {
		return _iniDep;
	}

	Departure getLastDeparture() {
		return _lastDep;
	}


	void addDeparture(Departure dep) {
		if (_depList.isEmpty()) {
			_iniDep = dep;
		}
		else {
			_lastDep = dep;
			_totalTime = Duration.between(_iniDep.getTime(), _lastDep.getTime());
		}
		_depList.add(dep);

	}

	Departure getDeparture(String station){
		for(Departure dep : _depList){
			if ( dep.getStation().toString().equals(station) ){
				return dep;
			}
		}
		return null;
	}

	String getServiceHeadline(){
		String result = "";
		result += "Serviço #" + _id + " @ ";

		return result;
	}


	String getServiceBody(Departure iniDep, Departure endDep){
		StringBuilder stringBuilder = new StringBuilder();

		for(int i = _depList.indexOf(iniDep); i <= _depList.indexOf(endDep); i++){
			stringBuilder.append(_depList.get(i));
			stringBuilder.append("\n");
		}

		return stringBuilder.toString();
	}


	void determinarItinerario(Station iniStation, Station lastStation, LocalDate date, LocalTime time, Map<String, Station> stationMap, Map<Integer, Service> serviceMap, List<Segment> segList, List<Itinerary> itiList){

		ArrayList<Departure> departureList;

		Departure iniDep = null;
		Departure lastDep = null;

		for(Departure d : _depList){    //Procura se existem as estações de partida e chegadas dadas como argumento no serviço

			if(d.getStation() == iniStation){
				iniDep = d;
			}

			if(d.getStation() == lastStation){
				lastDep = d;
			}
		}

		if(_depList.indexOf(iniDep) + 1 == _depList.size()){  //Verifica se a estacao de partida é a ultima  do serviço
			return;																							//Se sim, pára a iteração.
		}

		if(iniDep != null && iniDep.getTime().isBefore(time)){ //Verifica se a estação de partida tem hora depois da hora minima dada como argumento.
			return;
		}


		if (lastDep == null){ //Compor uma sublista com todas as Departures após a estação de partida e até á de chegada.
			departureList = new ArrayList<>(_depList.subList(_depList.indexOf(iniDep) + 1, _depList.size() )); //caso nao existir estação de chegada, fim é list.size().
		}else{
			if(iniDep != null && iniDep.getTime().isAfter(lastDep.getTime())){ //Se existirem as duas estações, verifica se a estação de partida tem hora antes da de chegada.
				return;
			}
			departureList = new ArrayList<>(_depList.subList(_depList.indexOf(iniDep) + 1, _depList.indexOf(lastDep) + 1 ));
		}

		for(Departure departure: departureList){ //Verifica se já passou por este serviço ou se já passou alguma das estações da sublista, se sim, pára esta iteração.
			if((serviceMap.containsKey(departure.getService().getId()) || stationMap.containsKey(departure.getStation().toString()))){
				return;
			}
		}

		if(( iniDep != null ) && ( lastDep != null ) ){ //Caso não falhe as verificações e lastDep existe no serviço, existe um segmento que liga a estaçao de partida á de chegada

			Segment segment = new Segment(iniDep, lastDep, this); //cria um segmento com as estações e serviço
			segList.add(segment); //adiciona o elemento á lista
			List<Segment> segCopy = new ArrayList<>(segList); //Efectuamos uma cópia integral
			itiList.add(new Itinerary(date,segCopy)); //Criamos um itinerario com date e time passados como argumento, e segmentos calculados até chegar aqui.
			segList.remove(segment);  //remove o ultimo segmento adicionado antes de passar para o calculo do proximo possivel itenerario.
			return;  //termina a iteração

		}else {

			for (Departure dep: departureList){ //Se nao exitir caminho directo, iteramos sobre a sublista de Departures criada anteriormente

				List<Departure> tempDep = dep.getStation().getDepList(); //Obtem a lista de departures que têm como estação, a estação da departure actual (da sublista)

				Segment segment = new Segment(iniDep, dep, this); //Adiciona o segmento antes de ver os possiveis itinerarios .
				segList.add(segment);
				addPassedStationsServices(segment, stationMap, serviceMap); //E adiciona o serviço e departures do segmento á lista de servicos e departure já consideradas
				for (Departure departure: tempDep){ //para cada uma das departures que têm a mesma estação
					if(serviceMap.containsKey(departure.getService().getId()) || departure.getTime().isBefore(dep.getTime()) ){ //retornamos se o servico já foi considerado ou se a departure a fazer a ligaçao entre serviços é depois da departure do serviço inicial
						continue; //por exemplo, se S1 - 10:00|A|11:10|B -> S2 - 09:00|B|9:30|C, retorna.
					}
					departure.getService().determinarItinerario(dep.getStation(),lastStation,date,time, stationMap, serviceMap, segList,itiList);//Aplica o algoritmo recursivamente ao serviço de cada departure com estação igual.
				}
				segList.remove(segment); //remove o segmento antes de passar para a proxima iteração
				removePassedStationsServices(segment, stationMap, serviceMap); // e remove o serviço e departures deste segmento da lista dos objectos já considerados
			}

		}

	}



	private void addPassedStationsServices(Segment segment, Map<String, Station> stationMap,  Map<Integer, Service> serviceMap){
		Service service = segment.getService();
		if(!serviceMap.containsKey(service.getId())){
			serviceMap.put(service.getId(),service);
		}

		List<Departure> departureList = new ArrayList<>(_depList.subList(_depList.indexOf(segment.getIniDep()), _depList.indexOf(segment.getEndDep()) ));

		for (Departure departure: departureList){
			if(!stationMap.containsKey(departure.getStation().toString())){
				stationMap.put(departure.getStation().toString(), departure.getStation());
			}
		}

	}

	private void removePassedStationsServices(Segment segment, Map<String, Station> stationMap, Map<Integer, Service> serviceMap){
		Service service = segment.getService();
		if(serviceMap.containsKey(service.getId())){
			serviceMap.remove(service.getId());
		}

		List<Departure> departureList = new ArrayList<>(_depList.subList(_depList.indexOf(segment.getIniDep()), _depList.indexOf(segment.getEndDep()) ));

		for (Departure departure: departureList){
			if(stationMap.containsKey(departure.getStation().toString())){
				stationMap.remove(departure.getStation().toString());
			}
		}

	}

	@Override
	public String toString() {

		String result = "";

		result += getServiceHeadline() + String.format("%.2f", _cost) + "\n";

		result += getServiceBody(_iniDep, _lastDep);

		return result;
	}
}
