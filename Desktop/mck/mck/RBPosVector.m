%a,b,c indicate the order for hip marker, foot marker and knee marker as
%they are ordered in the stream panel in Arena

%A 3x3 matris R is returned from which positions vectors, the columns, can be accessed

%WRITTEN FOR USE WITH THREE RIGID BODIES

function R=ThreeRBPosVector(a,b,c, natnetClient);

    rbh=[natnetClient.getFrame('rigidbody',a).RigidBody.x; natnetClient.getFrame('rigidbody',a).RigidBody.y; natnetClient.getFrame('rigidbody',a).RigidBody.z];
    rbf=[natnetClient.getFrame('rigidbody',b).RigidBody.x; natnetClient.getFrame('rigidbody',b).RigidBody.y; natnetClient.getFrame('rigidbody',b).RigidBody.z];
    rbk=[natnetClient.getFrame('rigidbody',c).RigidBody.x; natnetClient.getFrame('rigidbody',c).RigidBody.y; natnetClient.getFrame('rigidbody',c).RigidBody.z];
    
    R = [rbh rbf rbk];