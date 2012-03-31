
"""
All calls to the API will be translated to JSON via XSLT.
"""

from config import AWS_KEY, SECRET_KEY
from amazonproduct.api import API
from amazonproduct.errors import AWSError

import simplejson

class JSONAPI (API):
    
    """
    This API uses Doeke Zanstra's XSLT stylesheet which converts XML to JSON
    http://code.google.com/p/xml2json-xslt/
    """
    
    STYLE_SHEET = 'http://xml2json-xslt.googlecode.com/svn/trunk/xml2json.xslt'
    
    def __init__(self, *args, **kwargs):
        API.__init__(self, *args, **kwargs)
        self.processor = self.json_processor
    
    def _build_url(self, **qargs):
        qargs['Style'] = self.STYLE_SHEET
        return  API._build_url(self, **qargs)
    
    @staticmethod
    def json_processor(fp):
        """
        Returns response as Python dict.
        """
        def _find_errors(root):
            "Find Error nodes"
            if type(root) != dict: return 
            for parent, children in root.items():
                if parent == 'Error':
                    raise AWSError(children['Code'], children['Message'])
                _find_errors(children)
        parsed = simplejson.load(fp)
        _find_errors(parsed)
        return parsed
