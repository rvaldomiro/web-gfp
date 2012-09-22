package gfp.service
{
	import common.component.service.AbstractService;
	import common.component.service.IService;
	import common.custom.CustomRemoteObject;
	import common.custom.ICustomEvent;
	import common.util.RemoteUtil;
	
	import gfp.dto.UsuarioDto;
	import gfp.model.Usuario;
	
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class UsuarioService extends AbstractService
	{
		
		public function get idUsuarioLogado():Number
		{
			return usuarioLogado.id as Number;
		}
		
		[Bindable]
		public var selecionado:Usuario;
		
		[Bindable]
		public var usuarioLogado:Usuario;
		
		private function get service():RemoteObject
		{
			return RemoteUtil.createRemoteObject("usuarioService") as RemoteObject;
		}
		
		[EventHandler(event = "UsuarioEvent.LOGIN", properties = "event")]
		public function login(event:ICustomEvent):void
		{
			var dto:UsuarioDto = event.object as UsuarioDto;
			
			executeService(service.login(dto.login, dto.senha), function(re:ResultEvent):void
			{
				usuarioLogado = re.result as Usuario;
				event.result(re);
			}, event.fault);
		}
		
		[EventHandler(event = "UsuarioEvent.SALVAR", properties = "event")]
		public function salvar(event:ICustomEvent):void
		{
			executeService(service.salvarUsuario(selecionado), function(re:ResultEvent):void
			{
				event.result(re);
			}, event.fault);
		}
	}
}

