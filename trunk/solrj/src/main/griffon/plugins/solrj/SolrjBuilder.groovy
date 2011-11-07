/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package griffon.plugins.solrj

import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrQuery.ORDER
import org.apache.solr.client.solrj.beans.Field

/**
 *
 * @author commander
 */
class SolrjBuilder {
    
	def finalExpression = '' << ''
	def OR = ' || '
	def AND = ' && '
	def OPEN_GROUP = "("
	def CLOSE_GROUP = ")"
	def constraintsBuilder = new ConstrainsBuilder()
	def paginationExpando = new Expando()
	def resultClass
	
	def server
	def query = new SolrQuery()
	
	public SolrjBuilder(serverInstance){
            server = serverInstance		
	}
        
        /* If user wants to access directly to the solr server instance use this method
         * instead of createCriteria. */
        Object getSolrInstance(){
            server
        }
	
	/* Now if the method is missing is because is out of any other group and though 
	 * it should be added with an and expression */
	void methodMissing(String name,args){
		finalExpression << "${args[0]}:${args[1]}" << AND
	}
	
	/* Here an OR group expression is built with all its terms */
	Object or(closure){
		def terms = constraintsBuilder.build(closure)
		def orExpression = terms.join(OR)
		
		finalExpression << OPEN_GROUP << orExpression << CLOSE_GROUP
	}
	
	/* Here an AND group expression is built with all its terms */
	Object and(closure){
		def terms = constraintsBuilder.build(closure)
		def andExpression = terms.join(AND)
		
		finalExpression << OPEN_GROUP << andExpression << CLOSE_GROUP
	}
        
        Object resultClass(clazz){            
            resultClass = clazz
            finalExpression    
        }
	
	Object order(field,direction){
		paginationExpando.with{
			orderDirection = direction
			orderField = field
		}
		finalExpression
	}
	
	Object offset(offset){
		paginationExpando.offset = offset
		
		finalExpression
	}
	
	Object maxResults(maxResults){
		paginationExpando.maxResults = maxResults
		
		finalExpression
	}
	
	Object createCriteria(closure){
         /* Final expression should be reset */
                constraintsBuilder = new ConstrainsBuilder()         	     
                finalExpression = new StringBuffer()
	 /* Getting the query expression */
		def queryExpression = this.with(closure)
	 /* Setting metadata */
		paginationExpando.query = query
		paginationExpando.with{
			if (orderField){
				query.addSortField(
					orderField,orderDirection == 'desc' ? 
						ORDER.desc : 
						ORDER.asc
					)
			}
			query.
				setStart(offset?:0).
				setRows(maxResults?:10)			
		}
	 /* Getting the response */
                def q = queryExpression.toString()
                query.setQuery(q)
		def response = server.query(query)
             /* And return the result */
		response.getBeans(resultClass)             
	}
}

