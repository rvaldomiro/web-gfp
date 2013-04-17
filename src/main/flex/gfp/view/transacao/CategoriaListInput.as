package gfp.view.transacao
{
	import common.util.MessageUtil;
	
	import flash.events.IEventDispatcher;
	
	import gfp.event.CategoriaEvent;
	import gfp.model.Categoria;
	import gfp.service.UsuarioService;
	import gfp.type.CategoriaType;
	
	import mx.rpc.events.ResultEvent;
	
	import spark.components.ComboBox;
	
	public class CategoriaListInput extends ComboBox
	{
		
		[Dispatcher]
		public var dispatcher:IEventDispatcher;
		
		[Inject]
		public var usuarioService:UsuarioService;
		
		public function validate(callBack:Function):void
		{
			MessageUtil.confirmBoolean("'" + textInput.text + "' será uma categoria de Receitas?"
									   , function(result:Boolean):void
									   {
										   var categoria:Categoria = new Categoria();
										   categoria.descricao = textInput.text;
										   categoria.usuario = usuarioService.usuarioLogado;
										   categoria.tipo = result ? CategoriaType
											   .RECEITA : CategoriaType.DESPESA;
										   
										   dispatcher.dispatchEvent(new CategoriaEvent(CategoriaEvent
																					   .SALVAR
																					   , categoria
																					   , function(re:ResultEvent):void
																					   {
																						   
																						   selectedItem = re
																							   .result;
																						   dataProvider
																							   .addItem(re
																										.result);
																						   callBack();
																					   }));
									   });
		}
	}
}

