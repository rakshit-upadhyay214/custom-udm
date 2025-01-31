import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityCondition
import org.moqui.entity.EntityFind
import org.moqui.entity.EntityValue

import java.sql.Timestamp


ExecutionContext ec = context.ec
EntityFind ef = ec.entity.find("custom.udm.FindCustomerView")

ef.selectField("partyId")

if (partyId) {
    ef.condition(
            ec.entity
                    .conditionFactory
                    .makeCondition(
                    "partyId", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + partyId + "%"
                    ).ignoreCase())
}

if (firstName) { ef.condition(ec.entity.conditionFactory.makeCondition("firstName", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + firstName + "%").ignoreCase()) }

if (lastName) { ef.condition(ec.entity.conditionFactory.makeCondition("lastName", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + lastName + "%").ignoreCase()) }

if(emailAddress) {
    ef.condition(ec.entity.conditionFactory.makeCondition(
            (ec.entity.conditionFactory.makeCondition("emailAddress", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + emailAddress + "%").ignoreCase()),
            AND,
            (ec.entity.conditionFactory.makeCondition("purposeOfContact", EntityCondition.LIKE, (leadingWildcard ? "%" : "") + "PrimaryEmail" + "%"))
    ))
}

if(purposeOfContact) {ef.condition(ec.entity.conditionFactory.makeCondition("purposeOfContact", EntityCondition.LIKE, (leadingWildcard ?"%" : "")+ purposeOfContact + "%").ignoreCase())}

if(address) {ef.condition(ec.entity.conditionFactory.makeCondition("address", EntityCondition.LIKE,(leadingWildcard ? "%" : "")+address+"%").ignoreCase())}

if(city) {ef.condition(ec.entity.conditionFactory.makeCondition("city", EntityCondition.LIKE, (leadingWildcard ? "%" : "")+city+"%").ignoreCase())}

if(postalCode) {ef.condition(ec.entity.conditionFactory.makeCondition("postalCode", EntityCondition.LIKE,(leadingWildcard ? "%" : "")+postalCode+"%"))}

if(contactNumber) {ef.condition(ec.entity.conditionFactory.makeCondition("contactNumber", EntityCondition.LIKE,(leadingWildcard ? "%" : "")+contactNumber+"%"))}

if(thruDate) {ef.condition(ec.entity.conditionFactory.makeConditionDate("fromDate","thruDate",(new Timestamp(new Date().getTime()))))}


partyList = []
ef.orderBy("firstName")
EntityList allMatchingRecords = ef.list()
for (EntityValue ev in allMatchingRecords) partyList.add(ev.partyId)

listCount = ef.count()
listPageSize = ef.pageSize