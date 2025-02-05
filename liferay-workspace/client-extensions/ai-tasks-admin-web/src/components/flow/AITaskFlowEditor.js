import React, { useCallback, useEffect, useRef, useState } from 'react';
import {
    addEdge,
    Background,
    BackgroundVariant,
    Controls,
    MiniMap,
    Panel,
    ReactFlow,
    useEdgesState,
    useNodesState,
    useReactFlow,
} from '@xyflow/react';
import '@xyflow/react/dist/style.css';

import EntryPointNode from './nodes/EntryPointNode';
import GeminiChatModelNode from './nodes/GeminiChatModelNode';
import GoogleImagenNode from './nodes/GoogleImagenNode';
import LiferaySearchNode from './nodes/LiferaySearchNode';
import OllamaChatModelNode from './nodes/OllamaChatModelNode';
import OpenAIChatModelNode from './nodes/OpenAIChatModelNode';
import OpenAIImageModelNode from './nodes/OpenAIImageModelNode';
import WebhookNode from './nodes/WebhookNode';
import AITaskFlowContextMenu from './AITaskFlowContextMenu';
import { useAITasksContext } from '../../contexts/AITasksContext';
import AITaskFlowNodesPane from './AITaskFlowNodesPane';
import { useDnD } from '../../contexts/DnDContext';
import { adjectives, animals, colors, uniqueNamesGenerator } from 'unique-names-generator';
import AITaskFlowNodeConfigure from './AITaskFlowNodeConfigure';
import ModalFooterButtonGroup from '../ui/ModalFooterButtonGroup';
import { useModal } from '../../contexts/ModalContext';
import { toCamelCase } from '../../utils/stringUtils';
import {
    ENTRY_POINT_NODE,
    GEMINI_CHAT_MODEL,
    GOOGLE_IMAGEN,
    LIFERAY_SEARCH,
    OLLAMA_CHAT_MODEL,
    OPENAI_CHAT_MODEL,
    OPENAI_IMAGE_MODEL,
    WEBHOOK,
} from '../../constants/AITasksNodeTypesConstants';
import { getDefaultParameters } from '../../utils/nodeUtils';
import { v4 as uuidv4 } from 'uuid';
import AITaskFlowNodeRename from './AITaskFlowNodeRename';
import AITaskFlowNodeSetCondition from './AITaskFlowNodeSetCondition';
import AITaskFlowEdge from './edges/AITaskFlowEdge';
import { AI_TASK_FLOW_EDGE } from '../../constants/AITasksEdgeTypesConstants';
import Alert from '../ui/Alert';

const AITaskFlowEditor = ({ onConfigChange }) => {
    const { selectedTask, selectedNode, setSelectedNode } = useAITasksContext();
    const { setIsModalOpen, setModalTitle, setModalContent, setModalFooter } = useModal();
    const ref = useRef(null);
    const [nodes, setNodes, onNodesChange] = useNodesState([]);
    const [edges, setEdges, onEdgesChange] = useEdgesState([]);
    const [entryPointPosition, setEntryPointPosition] = useState(() => {
        const storedEntryPointPosition = localStorage.getItem(`${selectedTask.id}-entrypoint-position`);
        return storedEntryPointPosition ? JSON.parse(storedEntryPointPosition) : { x: 0, y: 0 };
    });
    const [menu, setMenu] = useState(null);
    const [isNodesPaneOpen, setIsNodesPaneOpen] = useState(false);
    const reactFlowWrapper = useRef(null);
    const { screenToFlowPosition } = useReactFlow();
    const [type, name] = useDnD();
    const selectedNodeRef = useRef(selectedNode);

    const nodeTypes = {
        [ENTRY_POINT_NODE]: EntryPointNode,
        [GEMINI_CHAT_MODEL]: GeminiChatModelNode,
        [GOOGLE_IMAGEN]: GoogleImagenNode,
        [LIFERAY_SEARCH]: LiferaySearchNode,
        [OLLAMA_CHAT_MODEL]: OllamaChatModelNode,
        [OPENAI_CHAT_MODEL]: OpenAIChatModelNode,
        [OPENAI_IMAGE_MODEL]: OpenAIImageModelNode,
        [WEBHOOK]: WebhookNode,
    };

    const edgeTypes = {
        [AI_TASK_FLOW_EDGE]: AITaskFlowEdge,
    };

    const getNodesFromConfig = (configuration) => {
        return configuration.nodes.map((node) => ({
            id: node.id,
            type: node.type,
            position: node.uiConfiguration?.position || { x: 0, y: 0 },
            data: {
                label: node.label,
                uiConfiguration: node.uiConfiguration,
                parameters: node.parameters,
                condition: node.condition,
            },
        }));
    };

    const getEdgesFromConfig = (configuration) => {
        return configuration.edges.map((edge) => ({
            id: edge.id,
            type: AI_TASK_FLOW_EDGE,
            source: edge.source,
            target: edge.target,
        }));
    };

    const getStartNode = (initialNodes, startNodeId) => {
        return initialNodes.find((node) => node.id === startNodeId);
    };

    const getEntryPointNode = (startNode) => {
        let position = entryPointPosition;
        if (startNode && entryPointPosition.x === 0 && entryPointPosition.y === 0) {
            position = {
                x: startNode.position.x - 300,
                y: startNode.position.y,
            };
        }
        localStorage.setItem(`${selectedTask.id}-entrypoint-position`, JSON.stringify(position));
        return {
            id: 'entryPoint',
            type: ENTRY_POINT_NODE,
            position: {
                x: position.x,
                y: position.y,
            },
            data: { label: 'User Input' },
        };
    };

    const getEntryPointEdge = (startNodeId) => {
        return {
            id: 'entryPointEdge',
            source: 'entryPoint',
            target: startNodeId,
            style: {
                strokeDasharray: 6,
                strokeWidth: 2,
                stroke: '#80acff',
            },
        };
    };

    const handleNodesChange = useCallback(
        (changes) => {
            onNodesChange(changes);
        },
        [nodes, onNodesChange],
    );

    const handleEdgesChange = useCallback(
        (changes) => {
            onEdgesChange(changes);
            const edgeIdsToRemove = changes
                .filter((change) => change.type === 'remove')
                .map((change) => change.id);
            if (edgeIdsToRemove && edgeIdsToRemove.length > 0) {
                const updatedEdges = selectedTask.configuration.edges.filter((edge) => {
                    return !edgeIdsToRemove.includes(edge.id);
                });
                onConfigChange({
                    ...selectedTask.configuration,
                    edges: updatedEdges,
                });
            }
        },
        [edges, onEdgesChange],
    );

    const handleNodeDragStop = useCallback(
        (event, node) => {
            const updatedNodes = selectedTask.configuration.nodes.map((configNode) => {
                if (configNode.id === node.id) {
                    return {
                        ...configNode,
                        uiConfiguration: {
                            ...configNode.uiConfiguration,
                            position: node.position,
                        },
                    };
                }
                return configNode;
            });

            if (node.id === 'entryPoint') {
                localStorage.setItem(
                    `${selectedTask.id}-entrypoint-position`,
                    JSON.stringify(node.position),
                );
                setEntryPointPosition(node.position);
            }

            onConfigChange({
                ...selectedTask.configuration,
                nodes: updatedNodes,
            });
        },
        [selectedTask.configuration, onConfigChange],
    );

    const handleGraphUpdate = useCallback(
        (newNodes, newEdges) => {
            const filteredNodes = newNodes.filter((newNode) => newNode.id !== 'entryPoint');
            const updatedNodes = filteredNodes.map((newNode) => {
                return {
                    id: newNode.id,
                    label: newNode.label || newNode.data.label,
                    type: newNode.type,
                    condition: newNode.condition || null,
                    parameters: newNode.parameters || newNode.data.parameters,
                    uiConfiguration: newNode.uiConfiguration || newNode.data.uiConfiguration,
                };
            });
            const updatedEdges = newEdges
                .filter((newEdge) => newEdge.id !== 'entryPointEdge')
                .map((edge) => {
                    return {
                        id: edge.id,
                        source: edge.source,
                        target: edge.target,
                    };
                });

            const updatedNodesIds = updatedNodes.map((updatedNode) => {
                return updatedNode.id;
            });

            let startNodeId = '';

            if (updatedNodesIds.includes(selectedTask.configuration.startNodeId)) {
                startNodeId = selectedTask.configuration.startNodeId;
            }

            onConfigChange({
                ...selectedTask.configuration,
                nodes: updatedNodes,
                edges: updatedEdges,
                startNodeId,
            });
        },
        [selectedTask, onConfigChange],
    );

    const onNodeContextMenu = useCallback(
        (event, node) => {
            event.preventDefault();
            event.stopPropagation();
            const pane = ref.current.getBoundingClientRect();
            setSelectedNode(selectedTask.configuration.nodes.find((n) => n.id === node.id));
            setMenu({
                id: node.id,
                top: event.clientY < pane.height - 200 && event.clientY - (39 + 79 + 24 + 56), // the offset in parentheses is the sum of paddings, margins and heights above the pane
                left: event.clientX < pane.width - 200 && event.clientX + 10,
                right: event.clientX >= pane.width - 200 && pane.width - event.clientX,
                bottom: event.clientY >= pane.height - 200 && pane.height - event.clientY,
            });
        },
        [setMenu, selectedTask],
    );

    const onPaneClick = useCallback(() => setMenu(null), [setMenu]);

    const onDragOver = useCallback((event) => {
        event.preventDefault();
        event.dataTransfer.dropEffect = 'move';
    }, []);

    const onDrop = useCallback(
        (event) => {
            event.preventDefault();

            if (!type) {
                return;
            }

            const position = screenToFlowPosition({
                x: event.clientX - 150,
                y: event.clientY - 25,
            });

            const nodeLabel = uniqueNamesGenerator({
                dictionaries: [colors, adjectives, animals],
                separator: ' ',
                style: 'capital',
            });

            const nodeId = toCamelCase(nodeLabel);

            const newNode = {
                id: nodeId,
                type,
                label: nodeLabel,
                parameters: getDefaultParameters(type),
                uiConfiguration: { position },
            };

            const updatedNodes = [...selectedTask.configuration.nodes, newNode];

            onConfigChange({
                ...selectedTask.configuration,
                nodes: updatedNodes,
            });
        },
        [screenToFlowPosition, type, selectedTask, onConfigChange, setNodes],
    );

    const onConnect = useCallback(
        (connection) => {
            if (connection.source === 'entryPoint') {
                onConfigChange({
                    ...selectedTask.configuration,
                    startNodeId: connection.target,
                });
            } else {
                const updatedConfig = {
                    ...selectedTask.configuration,
                    edges: [
                        ...selectedTask.configuration.edges,
                        {
                            id: uuidv4(),
                            source: connection.source,
                            target: connection.target,
                        },
                    ],
                };
                onConfigChange(updatedConfig);
            }
            setEdges((eds) => addEdge(connection, eds));
        },
        [selectedTask, onConfigChange],
    );

    const renderModalFooter = () => (
        <ModalFooterButtonGroup
            onConfirm={() => {
                const newNodes = selectedTask.configuration.nodes.map((node) => {
                    if (node.id === selectedNodeRef.current.id) {
                        return {
                            ...selectedNodeRef.current,
                            label: selectedNodeRef.current.label.trim(),
                        };
                    }
                    return node;
                });
                handleGraphUpdate(newNodes, selectedTask.configuration.edges);
            }}
        ></ModalFooterButtonGroup>
    );

    const openConfigureModal = (e) => {
        e.preventDefault();
        setModalTitle(`Configure ${selectedNodeRef.current.label}`);
        setModalContent(() => <AITaskFlowNodeConfigure />);
        setModalFooter(() => renderModalFooter());
        setIsModalOpen(true);
    };

    const openRenameModal = (e) => {
        e.preventDefault();
        setModalTitle(`Rename ${selectedNodeRef.current.label}`);
        setModalContent(() => <AITaskFlowNodeRename />);
        setModalFooter(() => renderModalFooter());
        setIsModalOpen(true);
    };
    const openSetConditionModal = (e) => {
        e.preventDefault();
        setModalTitle(`Set Condition to ${selectedNodeRef.current.label}`);
        setModalContent(() => <AITaskFlowNodeSetCondition />);
        setModalFooter(() => renderModalFooter());
        setIsModalOpen(true);
    };

    useEffect(() => {
        selectedNodeRef.current = selectedNode;
        const initialNodes = getNodesFromConfig(selectedTask.configuration);
        const initialEdges = getEdgesFromConfig(selectedTask.configuration);
        const startNode = getStartNode(initialNodes, selectedTask.configuration.startNodeId);
        const entryPointNode = getEntryPointNode(startNode);
        const entryPointEdge = getEntryPointEdge(selectedTask.configuration.startNodeId);
        setNodes([entryPointNode, ...initialNodes]);
        setEdges([entryPointEdge, ...initialEdges]);
    }, [selectedTask.configuration, selectedNode]);

    return (
        <div ref={reactFlowWrapper} className="flow-container d-flex flex-row">
            <AITaskFlowNodesPane isOpen={isNodesPaneOpen} setIsOpen={setIsNodesPaneOpen} />
            <ReactFlow
                ref={ref}
                nodes={nodes}
                edges={edges}
                nodeTypes={nodeTypes}
                edgeTypes={edgeTypes}
                onConnect={onConnect}
                onNodesChange={handleNodesChange}
                onEdgesChange={handleEdgesChange}
                onNodeDragStop={handleNodeDragStop}
                onPaneClick={onPaneClick}
                onNodeContextMenu={onNodeContextMenu}
                onDrop={onDrop}
                onDragOver={onDragOver}
                maxZoom={1.5}
                fitView
            >
                {menu && (
                    <AITaskFlowContextMenu
                        handleGraphUpdate={handleGraphUpdate}
                        onRename={openRenameModal}
                        onConfigure={openConfigureModal}
                        onSetCondition={openSetConditionModal}
                        onClick={onPaneClick}
                        {...menu}
                    />
                )}
                <Panel position="top-left">
                    <button
                        className={'btn btn-primary btn-lg mt-2'}
                        onClick={(e) => {
                            e.preventDefault();
                            setIsNodesPaneOpen(!isNodesPaneOpen);
                        }}
                    >
                        Add Nodes
                    </button>
                </Panel>
                <Panel position="top-center">
                    {!selectedTask.configuration.startNodeId && (
                        <Alert
                            type={'warning'}
                            message={`Connect the user input to a node to enable processing.`}
                        />
                    )}
                </Panel>
                <Background
                    variant={BackgroundVariant.Dots}
                    bgColor={'#f1f2f5'}
                    color={'#c0c1c3'}
                    gap={12}
                />
                <Controls showInteractive={false} />
                <MiniMap position={'bottom-left'} nodeStrokeColor={'#80acff'} nodeStrokeWidth={8} />
            </ReactFlow>
        </div>
    );
};

export default AITaskFlowEditor;
