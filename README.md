# maskinporten-mulesoft

Mule application for connecting to maskinporten (https://samarbeid.digdir.no/maskinporten/maskinporten/25)

A custom java method is needed to generate the token towards maskinporten, since dataweave from mulesoft does not yet support RSA-SHA256 alg.

Source:
1. https://docs.digdir.no/maskinporten_protocol_jwtgrant.html
2. https://docs.mulesoft.com/mule-runtime/4.3/dw-crypto




This application utilizes an already existing maven package, developed by KS (https://github.com/ks-no/fiks-maskinporten)


# How to use

Change config.yaml, under 'maskinporten' element with the appropriate values

You can either use the Java connector from mulesoft to call the method directly or use the Transform Message module to call it. 

This application shows both examples:

**Transform Message**
```
<flow name="getToken-transform-message" doc:id="2e750792-e447-4ea6-9d8b-7c173a5fe083">
	<ee:transform doc:name="Transform Message" doc:id="f5b0466d-d9cf-472c-885c-fa13ef4d755c">
		<ee:message>
			<ee:set-payload>
				<![CDATA[%dw 2.0
				import java!org::mycompany::utils::Maskinporten
				output application/json
				---
				{
					token: Maskinporten::getToken(p('maskinporten.filename'), p('maskinporten.pwd'), p('maskinporten.alias'), p('maskinporten.issuer'), p('maskinporten.audience'), p('maskinporten.tokenEndpoint'), p('maskinporten.scope'))
				}
				]]>
			</ee:set-payload>
		</ee:message>
	</ee:transform>
</flow>
```

**Java Connector**
```
<flow name="getToken-java-static" doc:id="7c6e32aa-8dad-467f-8c22-092720752093">
	<java:invoke-static method="getToken(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)" doc:name="Invoke static" doc:id="e1c89c59-bec1-4328-af5e-ea5204e78225" class="org.mycompany.utils.Maskinporten">
	<java:args>
	<![CDATA[#[
		{
			arg0: p('maskinporten.filename'), 
			arg1: p('maskinporten.pwd'), 
			arg2: p('maskinporten.alias'), 
			arg3: p('maskinporten.issuer'),
			arg4: p('maskinporten.audience'), 
			arg5: p('maskinporten.tokenEndpoint'), 
			arg6: p('maskinporten.scope')
		}
	]]]>
	</java:args>
	</java:invoke-static>
</flow>
```
