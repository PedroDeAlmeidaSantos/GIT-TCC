package br.net.daumhelp.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.net.daumhelp.dto.ClienteDTO;
import br.net.daumhelp.dto.ProfissionalDTO;
import br.net.daumhelp.dto.repository.ClienteDTORepository;
import br.net.daumhelp.dto.repository.ProfissionalDTORepository;
import br.net.daumhelp.model.CodeStatusPedido;
import br.net.daumhelp.model.Pedido;
import br.net.daumhelp.model.StatusPedido;
import br.net.daumhelp.repository.PedidoRepository;
import br.net.daumhelp.repository.StatusPedidosRepository;

@CrossOrigin
//@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "http://ec2-3-220-68-195.compute-1.amazonaws.com")
@RestController
@RequestMapping("/pedidos")
public class PedidosResource {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private ProfissionalDTORepository proDTORepository;
	
	@Autowired
	private ClienteDTORepository clienteDTORepository;
	
	@Autowired
	private StatusPedidosRepository statusRepository;

	
	@GetMapping
	public List<Pedido> getPedidos() {
		return pedidoRepository.findAll();
	}
	
	//POR ID
	@GetMapping("/id/{idPedido}")
	public Optional<Pedido> getPedidoById(@PathVariable Long idPedido){
		return pedidoRepository.findById(idPedido);
	}
	
	//POR CLIENTE
	@GetMapping("/cliente/{idCliente}")
	public List<Pedido> getPedidoByCliente(@PathVariable Long idCliente){
		return pedidoRepository.buscarPorCliente(idCliente);
	}
	
	//POR CLIENTE E STATUS
	@GetMapping("/cliente/{idCliente}/status/{idStatusPedido}")
	public List<Pedido> getPedidoByCliente(@PathVariable Long idCliente, @PathVariable Long idStatusPedido){
		return pedidoRepository.buscarPorClienteStatus(idCliente, idStatusPedido);
	}
	
	//TODOS DE UM CLIENTE NÃO ENCERRADOS
	@GetMapping("/cliente/{idCliente}/status/{idStatusOrcado}/{idStatusRejeitado}")
	public List<Pedido> getPedidosParaCliente(
			@PathVariable Long idCliente, 
			@PathVariable Long idStatusOrcado,
			@PathVariable Long idStatusRejeitado){
		return pedidoRepository.buscarParaCliente(idCliente, idStatusOrcado, idStatusRejeitado);
	}
	
	//POR PRO
	@GetMapping("/profissional/{idProfissional}")
	public List<Pedido> getPedidoByPro(@PathVariable Long idProfissional){
		return pedidoRepository.buscarPorPro(idProfissional);
	}
	
	//POR PRO E STATUS
	@GetMapping("/profissional/{idProfissional}/status/{idStatusPedido}")
	public List<Pedido> getPedidoByPro(@PathVariable Long idProfissional, @PathVariable Long idStatusPedido){
		return pedidoRepository.buscarPorProfissionalStatus(idProfissional, idStatusPedido);
	}
	
	//TODOS DE UM PRO NÃO ENCERRADOS
	@GetMapping("/profissional/{idProfissional}/status/{idStatusSolicitado}/{idStatusAceito}/{idStatusRejeitado}")
	public List<Pedido> getPedidosParaProfissional(
			@PathVariable Long idProfissional, 
			@PathVariable Long idStatusSolicitado,
			@PathVariable Long idStatusAceito,
			@PathVariable Long idStatusRejeitado){
		return pedidoRepository.buscarParaProfissional(idProfissional, idStatusSolicitado, idStatusAceito, idStatusRejeitado);
	}

	@PostMapping("/solicitar")
	public ResponseEntity<Pedido> solicitarPedido(
			@RequestBody Pedido pedido,
			HttpServletResponse response){

		pedido.setValorServico(0.0);
		
		Pedido pedidoSalvo = pedidoRepository.save(pedido);
			
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.buildAndExpand(pedido.getIdPedido())
				.toUri();
		response.addHeader("Location", uri.toASCIIString());
		 
		return ResponseEntity.created(uri).body(pedido);
	}

	@PutMapping("/resposta/{idPedido}")
	public ResponseEntity<Pedido> respostaPedido(
			@RequestBody Pedido pedido,
			@PathVariable Long idPedido){

		//BUSCA OS USUARIO ENVOLVIDOS NO PEDIDO
		ProfissionalDTO proPedido = proDTORepository.findById(pedido.getProfissional().getIdProfissional()).get();
		ClienteDTO clientePedido = clienteDTORepository.findById(pedido.getCliente().getIdCliente()).get();
		
		//BUSCA O NOVO STATUS DO PEDIDO 
		StatusPedido statusPedido = statusRepository.findById((long)CodeStatusPedido.ORCADO.getValue()).get();
		pedido.setStatus(statusPedido);
		
		//DEFINE OS USUARIOS DO PEDIDO ATUAL
		pedido.setProfissional(proPedido);
		pedido.setCliente(clientePedido);
		
		//BUSCA PEDIDO NO BANCO
		Pedido pedidoSalvo = pedidoRepository.findById(idPedido).get();
		//DEFINE O VALOR DO SERVIÇO SEGUNDO O TEMPO DE DURAÇÃO DEFINIDO PELO PROFISSIONAL 
		pedido.setValorServico(pedido.getHorasServico()*pedido.getProfissional().getValorHora());
		
		//ATUALIZA OS DADOS DO PEDIDO E SALVA NO BANCO
		BeanUtils.copyProperties(pedido, pedidoSalvo, "idPedido", "criadoEm", "atualizadoEm", "cliente", "profissional");
		pedidoRepository.save(pedidoSalvo);
		
		return ResponseEntity.ok(pedidoSalvo);
	}

	@PutMapping("/aceitar/{idPedido}")
	public ResponseEntity<Pedido> aceitarPedido(@PathVariable Long idPedido){
		
		Pedido pedido = pedidoRepository.findById(idPedido).get();
		
		//DEFINE O NOVO STATUS DO PEDIDO ATUAL
		StatusPedido statusPedido = statusRepository.findById((long)CodeStatusPedido.ACEITO.getValue()).get();		
		pedido.setStatus(statusPedido);
		
		System.out.println("--------ACEITOO------" + statusPedido);
		
		Pedido pedidoSalvo = pedidoRepository.findById(idPedido).get();
		
		BeanUtils.copyProperties(pedido, pedidoSalvo, "idPedido", "criadoEm", "atualizadoEm", "cliente", "profissional");
		pedidoRepository.save(pedidoSalvo);
		
		return ResponseEntity.ok(pedido);
	}

	@PutMapping("/cliente/rejeitar/{idPedido}")
	public ResponseEntity<Pedido> clienteRejeitarPedido(@PathVariable Long idPedido){
		
		Pedido pedido = pedidoRepository.findById(idPedido).get();

		//DEFINE O NOVO STATUS DO PEDIDO ATUAL
		StatusPedido statusPedido = statusRepository.findById((long)CodeStatusPedido.CANCELADO_CLIENTE.getValue()).get();
		pedido.setStatus(statusPedido);
		
		Pedido pedidoSalvo = pedidoRepository.findById(idPedido).get();
		
		BeanUtils.copyProperties(pedido, pedidoSalvo, "idPedido", "criadoEm", "atualizadoEm", "cliente", "profissional");
		pedidoRepository.save(pedidoSalvo);
		
		return ResponseEntity.ok(pedido);
	}
	@PutMapping("/profissional/rejeitar/{idPedido}")
	public ResponseEntity<Pedido> proRejeitarPedido(@PathVariable Long idPedido){
		
		Pedido pedido = pedidoRepository.findById(idPedido).get();

		//DEFINE O NOVO STATUS DO PEDIDO ATUAL
		StatusPedido statusPedido = statusRepository.findById((long)CodeStatusPedido.REJEITADO.getValue()).get();
		pedido.setStatus(statusPedido);
		
		System.out.println("--------REJEITADOOO------" + statusPedido);
		
		Pedido pedidoSalvo = pedidoRepository.findById(idPedido).get();
		
		BeanUtils.copyProperties(pedido, pedidoSalvo, "idPedido", "criadoEm", "atualizadoEm", "cliente", "profissional");
		pedidoRepository.save(pedidoSalvo);
		
		return ResponseEntity.ok(pedido);
	}
	
	@PutMapping("/{usuario}/cancelar/{idPedido}")
	public ResponseEntity<Pedido> proClientePedido(@PathVariable String usuario, @PathVariable Long idPedido){
		
		Pedido pedido = pedidoRepository.findById(idPedido).get();
		StatusPedido statusPedido = pedido.getStatus();

		System.out.println("--------CANCELADOO------" + statusPedido);
		
		if(usuario.equals("cliente")) {
			ProfissionalDTO proPedido = proDTORepository.findById(pedido.getProfissional().getIdProfissional()).get();
			
			//STATUS DO PEDIDO QUE É CANCELADO PELO CLIENTE
			statusPedido = statusRepository.findById((long)CodeStatusPedido.CANCELADO_CLIENTE.getValue()).get();
			
			//CALCULA MULTA DE CANCELAMENTO A SER PAGA QUA DO PEDIDO É CANCELADO PELO CLEINTE
			pedido.setMultaCliente(proPedido.getValorHora()*0.2);	
			
			 
		}else if(usuario.equals("profissional")){
			//STATUS DO PEDIDO QUE É CANCELADO PELO PROFISSIONAL
			statusPedido = statusRepository.findById((long)CodeStatusPedido.CANCELADO_PROFISSIONAL.getValue()).get();		
		}
		
		pedido.setStatus(statusPedido);
		
		Pedido pedidoSalvo = pedidoRepository.findById(idPedido).get();
		
		BeanUtils.copyProperties(pedido, pedidoSalvo, "idPedido", "criadoEm", "atualizadoEm", "cliente", "profissional");
		pedidoRepository.save(pedidoSalvo);
		
		return ResponseEntity.ok(pedido);
	}
	
	@PutMapping("/concluir/{idPedido}")
	public ResponseEntity<Pedido> concluirPedido(@PathVariable Long idPedido){
		
		Pedido pedido = pedidoRepository.findById(idPedido).get();

		//DEFINE O NOVO STATUS DO PEDIDO ATUAL		
		StatusPedido statusPedido = statusRepository.findById((long)CodeStatusPedido.CONCLUIDO.getValue()).get();
		pedido.setStatus(statusPedido);
		
		Pedido pedidoSalvo = pedidoRepository.findById(idPedido).get();
		
		BeanUtils.copyProperties(pedido, pedidoSalvo, "idPedido", "criadoEm", "atualizadoEm", "cliente", "profissional");
		pedidoRepository.save(pedidoSalvo);
		
		return ResponseEntity.ok(pedido);
	}
	
	
}
