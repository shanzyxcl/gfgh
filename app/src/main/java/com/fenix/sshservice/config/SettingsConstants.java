package com.fenix.sshservice.config;

public interface SettingsConstants {

	String TUNNELTYPE_KEY = "PgL3l0d",
			SERVIDOR_KEY = "ssz7ImP",
			SERVIDOR_PORTA_KEY = "P9ZUEM1",
			PROXY_IP_KEY = "Ghv856S",
			PROXY_PORTA_KEY = "WMY4zuZ",
			PROXY_USAR_DEFAULT_PAYLOAD = "Es8cZMN",
			CUSTOM_PAYLOAD_KEY = "8DoP4cN",
			CUSTOM_SNI = "JjCnpqW",
			USUARIO_KEY = "RoUaNaJ",
			SENHA_KEY = "GFM5lWW",
			CHAVE_KEY = "pfPQmdR",
			NAMESERVER_KEY = "P19AbQu",
			DNS_KEY = "YtkyRlV",
			UDP_SERVER = "vfVafVd",
			UDP_PORT = "Dj6tuBT",
			UDP_AUTH = "1FFSWRZ",
			UDP_OBFS = "NLZxFwN",
			UDP_BUFFER = "SbWiCK1",
			UDP_DOWN = "J9f3gfl",
			UDP_UP = "w364P3Z",
			V2RAY_CONFIG_KEY = "ZYuHTCH";

	int bTUNNEL_TYPE_SSH_DIRECT = 1,
			bTUNNEL_TYPE_SSH_PROXY = 2,
			bTUNNEL_TYPE_SSH_SSL = 3,
			bTUNNEL_TYPE_PAY_SSL = 4,
			bTUNNEL_TYPE_SLOWDNS = 5,
			bTUNNEL_TYPE_UDP = 6,
			bTUNNEL_TYPE_V2RAY = 7,
			type_ssh = 1,
			type_slowdns = 2,
			type_udp = 3,
			type_v2ray = 4,
			direct = 1,
			proxy = 2,
			ssl = 3,
			sslpay = 4,
			tls = 5,
			notls = 6;

	// Geral
	public static final String
	AUTO_CLEAR_LOGS_KEY = "autoClearLogs",
	HIDE_LOG_KEY = "hideLog",
	MODO_DEBUG_KEY = "modeDebug",
    MODE_PREMIUM_KEY = "modePremium",
	MODO_NOTURNO_KEY = "modeNight",
	BLOQUEAR_ROOT_KEY = "blockRoot",
	IDIOMA_KEY = "idioma",
	URL_KEY = "url",
	TETHERING_SUBNET = "tetherSubnet",
	DISABLE_DELAY_KEY = "disableDelaySSH",
	MAXIMO_THREADS_KEY = "numberMaxThreadSocks",
	SSH_COMPRESSION = "data_compression",
			AUTO_PINGER = "auto_ping",
			PINGER = "ping_server",


    WAKELOCK_KEY = "wakelock",
	//AUTO_PINGER = "auto_ping",
	NETWORK_SPEED = "speed_meter",

	FILTER_APPS = "filterApps",
	FILTER_BYPASS_MODE = "filterBypassMode",
	FILTER_APPS_LIST = "filterAppsList",
	PROXY_USAR_AUTENTICACAO_KEY = "usarProxyAutenticacao"
	;

	public static final String
	CONFIG_PROTEGER_KEY = "protegerConfig",
	CONFIG_MENSAGEM_KEY = "mensagemConfig",
	CONFIG_VALIDADE_KEY = "validadeConfig",
	CONFIG_MENSAGEM_EXPORTAR_KEY = "mensagemConfigExport",
	CONFIG_INPUT_PASSWORD_KEY = "inputPassword"
	;

	// Vpn
	public static final String
	DNSFORWARD_KEY = "dnsForward",
	DNSRESOLVER_KEY = "dnsResolver",
	BYPASS_KEY = "bypassKey",
	UDPFORWARD_KEY = "udpForward",
	UDPRESOLVER_KEY = "udpResolver";

	// SSH
	public static final String
	KEYPATH_KEY = "keyPath",
	PORTA_LOCAL_KEY = "sshPortaLocal",
	PINGER_KEY = "pingerSSH";
	//PINGER = "ping_server";
	// Tunnel Type
	public static final String
	TUNNEL_TYPE_SSH_DIRECT = "sshDirect",
	TUNNEL_TYPE_SSH_PROXY = "sshProxy",
	TUNNEL_TYPE_SSH_SSLTUNNEL = "sshSslTunnel";
}
