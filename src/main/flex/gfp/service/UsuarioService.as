package gfp.service
{
	import common.components.service.AbstractService;
	import common.components.service.IService;
	import common.custom.CustomRemoteObject;
	import common.custom.ICustomEvent;
	
	import gfp.dto.UsuarioDto;
	import gfp.model.Usuario;
	
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class UsuarioService extends AbstractService implements IService
	{
		
		public function get idUsuarioLogado():Number
		{
			return usuarioLogado.id as Number;
		
		}
		
		[Bindable]
		public var selecionado:Usuario;
		
		public function get service():RemoteObject
		{
			return new CustomRemoteObject("usuarioService");
		}
		
		[Bindable]
		public var usuarioLogado:Usuario;
		
		[EventHandler(event="UsuarioEvent.LOGIN")]
		public function login(event:ICustomEvent):void
		{
			var dto:UsuarioDto = event.object as UsuarioDto;
			
			executeService(service.login(dto.login, dto.senha), event, function(re:ResultEvent):void
			{
				usuarioLogado = re.result as Usuario;
				event.result(re);
			});
		}
		
		[EventHandler(event="UsuarioEvent.SALVAR")]
		public function salvar(event:ICustomEvent):void
		{
			executeService(service.salvarUsuario(selecionado), event, function(re:ResultEvent):void
			{
				event.result(re);
			});
		}
	}
}