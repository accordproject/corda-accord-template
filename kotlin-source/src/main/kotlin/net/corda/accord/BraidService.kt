package net.corda.accord

import net.corda.accord.state.PromissoryNoteState
import net.corda.core.node.ServiceHub

import javax.json.Json
import javax.json.JsonArrayBuilder
import javax.json.JsonObjectBuilder

class BraidService internal constructor(internal var serviceHub: ServiceHub) {
    val issuedPromissoryNotes: String
        get() {
            val allStatesJSONArrayBuilder = Json.createArrayBuilder()

            serviceHub.vaultService.queryBy(PromissoryNoteState::class.java).states.stream().forEach { (state) ->
                val objectBuilder = Json.createObjectBuilder()
                objectBuilder.add("MakerCordaParty", state.data.makerCordaParty.name.toString())
                objectBuilder.add("LenderCordaParty", state.data.lenderCordaParty.name.toString())
                objectBuilder.add("AmountQuantity", state.data.amount.quantity.toDouble() / 100.00)
                objectBuilder.add("AmountToken", state.data.amount.token.toString())
                objectBuilder.add("LinearId", state.data.linearId.toString())
                objectBuilder.add("owner", this.serviceHub.myInfo.legalIdentities[0].name.toString())
                allStatesJSONArrayBuilder.add(objectBuilder.build().toString())
            }

            return allStatesJSONArrayBuilder.build().toString()
        }

}
